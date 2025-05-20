package Example_Screen.Model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Aprendiz {
    private String nombre;
    private LocalDate fecha_fin_lec;
    private LocalDate fechaFin;

    public Aprendiz(String nombre, LocalDate fecha_fin_lec, LocalDate fechaFin) {
        this.nombre = nombre;
        this.fecha_fin_lec = fecha_fin_lec;
        this.fechaFin = fechaFin;
    }

    public String getNombre() { return nombre; }
    public LocalDate getFecha_fin_lec() { return fecha_fin_lec; }
    public LocalDate getFechaFin() { return fechaFin; }

    public int calcularProgreso() {
        LocalDate hoy = LocalDate.now();
        if (hoy.isBefore(fecha_fin_lec)) return 0;
        if (hoy.isAfter(fechaFin)) return 100;

        long totalDias = ChronoUnit.DAYS.between(fecha_fin_lec, fechaFin);
        long diasTranscurridos = ChronoUnit.DAYS.between(fecha_fin_lec, hoy);

        return (int) ((diasTranscurridos * 100) / totalDias);
    }
}