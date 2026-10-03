public class Unidad {
    private int id;
    private String placa;
    private String modelo;
    private int capacidad;
    private String estado;

    public Unidad(int id, String placa, String modelo, int capacidad, String estado){
        this.id = id;
        this.placa = placa;
        this.modelo = modelo;
        this.capacidad = capacidad;
        this.estado = estado;
    }

    public int getId() { return id; }
    public String getPlaca() { return placa; }
    public String getModelo() { return modelo; }
    public int getCapacidad() { return capacidad; }
    public String getEstado() { return estado;}
}
