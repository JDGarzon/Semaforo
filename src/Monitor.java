package src;

import java.util.concurrent.Semaphore;
import java.util.Random;
import java.util.Stack;

/**
 * La clase Monitor representa al monitor encargado de ayudar a los estudiantes
 * en la solución
 * de sus tareas de programación. El monitor tiene la capacidad de dormir cuando
 * no hay estudiantes
 * esperando ayuda y despertar cuando un estudiante lo necesita.
 */
class Monitor extends Thread {
    private Semaphore monitorSem; // Semáforo para controlar el acceso al monitor
    private Semaphore studentSem; // Semáforo para controlar la espera de los estudiantes
    private Semaphore corridorSem; // Semáforo para controlar el acceso al corredor
    private boolean sleeping; // Indica si el monitor está dormido o despierto
    private Stack<Student> students; // Pila de estudiantes en espera de ayuda

    /**
     * Constructor de la clase Monitor.
     * 
     * @param monitorSem  Semáforo para controlar el acceso al monitor.
     * @param studentSem  Semáforo para controlar la espera de los estudiantes.
     * @param corridorSem Semáforo para controlar el acceso al corredor.
     * @param students    Pila de estudiantes en espera de ayuda.
     */
    public Monitor(Semaphore monitorSem, Semaphore studentSem, Semaphore corridorSem, Stack<Student> students) {
        this.monitorSem = monitorSem;
        this.studentSem = studentSem;
        this.corridorSem = corridorSem;
        this.sleeping = true; // El monitor inicia durmiendo
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

    /**
     * Método principal del hilo del monitor.
     * El monitor permanece en un bucle infinito, durmiendo hasta que un estudiante
     * lo despierte.
     * Una vez despierto, atiende a los estudiantes en espera
     */
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
                    students.pop(); // Se atiende al estudiante que lo llamó
                }
                sleeping = false;

                // Atender a los estudiantes mientras haya en espera
                while (studentSem.availablePermits() == 0) {
                    System.out.println("Monitor: Ayudando a un estudiante.");
                    Thread.sleep(new Random().nextInt(MonitorDormilon.MAX_TIME)); // Simular tiempo de ayuda
                    studentSem.release();
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
