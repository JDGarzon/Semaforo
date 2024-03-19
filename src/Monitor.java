package src;

import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.Stack;

class Monitor extends Thread {
    private Semaphore monitorSem;
    private Semaphore studentSem;
    private Semaphore corridorSem;
    private boolean sleeping;
    private Stack<Student> students;

    public Monitor(Semaphore monitorSem, Semaphore studentSem, Semaphore corridorSem, Stack<Student> students) {
        this.monitorSem = monitorSem;
        this.studentSem = studentSem;
        this.corridorSem = corridorSem;
        this.sleeping = true;
        this.students = students;
    }

    public Semaphore getMonitor() {
        return monitorSem;
    }

    public Semaphore getStudent() {
        return studentSem;
    }

    public Semaphore getCorridor() {
        return corridorSem;
    }

    public boolean getSleeping() {
        return sleeping;
    }

    public void run() {
        while (true) {
            try {
                // Monitor duerme hasta que un estudiante lo despierte
                monitorSem.acquire();
                System.out.println("Monitor: Zzz...");
                sleeping = true;
                monitorSem.release();

                // Monitor despierta cuando un estudiante lo llama
                studentSem.acquire();
                System.out.println("Monitor: ¡Despierta! ¿En qué puedo ayudarte?");
                if (students.size() > 0) {
                    students.pop();
                }

                sleeping = false;

                // Atender a los estudiantes mientras haya en espera
                while (studentSem.availablePermits() == 0) {
                    System.out.println("Monitor: Ayudando a un estudiante.");
                    Thread.sleep(new Random().nextInt(MonitorDormilon.MAX_TIME));
                    studentSem.release();
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}