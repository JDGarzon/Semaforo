package src;

import java.util.Stack;
import java.util.concurrent.Semaphore;

public class MonitorDormilon {
    // Representa las sillas en las cuales se sientan los estudiantes
    private static Stack<Student> students = new Stack<Student>();
    // Mazimo de sillas en el corredor
    public static final int CHAIRS = 3;
    // Tiempo máximo de espera para cada actividad
    public static final int MAX_TIME = 10000;
    // Número de estudiantes
    public static final int NUM_STUDENTS = 6;

    public static void main(String[] args) {
        // Instanciar los semáforos requeridos
        Semaphore monitorSem = new Semaphore(1, true);
        Semaphore studentSem = new Semaphore(0, true);
        Semaphore corridorSem = new Semaphore(3, true);

        // Instanciar el monitor
        Monitor monitor = new Monitor(monitorSem, studentSem, corridorSem, students);
        monitor.start();

        // Instanciar estudiantes
        int numStudents = NUM_STUDENTS;
        for (int i = 1; i <= numStudents; i++) {
            Student student = new Student(i, monitor, studentSem, corridorSem, students);
            student.start();
            ;
        }
    }
}
