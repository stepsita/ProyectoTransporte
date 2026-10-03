public class Itinerario {
    
    private int idItinerario;
    private String fecha;
    private String hora;
    private String estado;
    private int cupos;
    
    private String modeloUnidad;
    private String nombreRuta;
    private String tipoRuta;
    private String nombreChofer;

    public Itinerario(int idItinerario, String fecha, String hora, String estado, int cupos, 
                      String modeloUnidad, String nombreRuta, String tipoRuta, String nombreChofer) {
        this.idItinerario = idItinerario;
        this.fecha = fecha;
        this.hora = hora;
        this.estado = estado;
        this.cupos = cupos;
        this.modeloUnidad = modeloUnidad;
        this.nombreRuta = nombreRuta;
        this.tipoRuta = tipoRuta;
        this.nombreChofer = nombreChofer;
    }

    public int getIdItinerario() { return idItinerario; }

    public String getFecha() { return fecha; }

    public String getHora() { return hora; }

    public String getEstado() { return estado; }

    public int getCupos() { return cupos; }

    public String getModeloUnidad() { return modeloUnidad; }

    public String getNombreRuta() { return nombreRuta; }

    public String getTipoRuta() { return tipoRuta; }

    public String getNombreChofer() { return nombreChofer; }

    
    @Override
    public String toString() {
        return "Itinerario [" + tipoRuta + " - " + nombreRuta + " | Salida: " + hora + " | Cupos: " + cupos + " | Estado: " + estado + "]";
    }
}