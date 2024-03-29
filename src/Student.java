package src;

import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.Stack;

class Student extends Thread {
    private int id;
    private Monitor monitor;
    private Semaphore studentSem;
    private Semaphore corridorSem;
    private Stack<Student> students;

    /**
     * Constructor de la clase Student.
     * 
     * @param id          Identificador del estudiante.
     * @param monitor     Referencia al monitor.
     * @param studentSem  Semáforo para controlar la espera del estudiante.
     * @param corridorSem Semáforo para controlar el acceso al corredor.
     * @param students    Pila de estudiantes en espera de ayuda.
     */
    public Student(int id, Monitor monitor, Semaphore studentSem, Semaphore corridorSem, Stack<Student> students) {
        this.id = id;
        this.monitor = monitor;
        this.studentSem = studentSem;
        this.corridorSem = corridorSem;
        this.students = students;
    }

    /**
     * Método principal del hilo del estudiante.
     * El estudiante simula el tiempo de programación y luego decide si esperar al
     * monitor en el corredor
     * o volver más tarde si no hay sillas disponibles.
     */
    public void run() {
        try {
            while (true) {

                Thread.sleep(new Random().nextInt(MonitorDormilon.MAX_TIME));

                // Estudiante llega y necesita ayuda
                monitor.getMonitor().acquire();
                if (monitor.getSleeping()) {
                    System.out.println("Estudiante " + id + ": ¡Monitor, despierta!");
                    monitor.getMonitor().release();
                    studentSem.release();
                } else {

                    if (students.size() < MonitorDormilon.CHAIRS) {
                        monitor.getMonitor().release();
                        corridorSem.acquire();
                        students.push(this);
                        System.out.println("Estudiante " + id + ": Esperando en el corredor.");
                        Thread.sleep(new Random().nextInt(MonitorDormilon.MAX_TIME));
                        corridorSem.release();
                    } else {
                        System.out.println(
                                "Estudiante " + id + ": No hay sillas disponibles, me voy a la sala de computo.");

                        monitor.getMonitor().release();
                        Thread.sleep(new Random().nextInt(MonitorDormilon.MAX_TIME));
                    }

                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
