public class Sensor {

    private String id;
    private String ubicacion;
    private String unidad;
    private boolean activo;
    private double ultimaMedicion;

    public Sensor(String id, String ubicacion, String unidad) {
        this.id = id;
        this.ubicacion = ubicacion;
        this.unidad = unidad;
        this.activo = true;
        this.ultimaMedicion = 0.0;
    }

    public String getId() {
        return id;
    }

    public String getUbicacion() {
        return ubicacion;
    }

    public String getUnidad() {
        return unidad;
    }

    public boolean isActivo() {
        return activo;
    }

    public double getUltimaMedicion() {
        return ultimaMedicion;
    }

    public void activar() {
        activo = true;
    }

    public void desactivar() {
        activo = false;
    }

    /**
     * Guarda una nueva medición solo si el sensor está activo.
     *
     * @return true si la medición se registró; false si no.
     */
    public boolean registrarMedicion(double valor) {
        if (!activo) {
            return false;
        }
        ultimaMedicion = valor;
        return true;
    }

    /**
     * Versión general de la interpretación. Cada subclase la sobrescribe
     * con sus propios criterios.
     */
    public String evaluarMedicion() {
        return "Sin criterio de interpretación";
    }

    /**
     * Arma un texto con los datos del sensor y la interpretación de su medición.
     * Como llama a evaluarMedicion(), cada tipo de sensor muestra su propia
     * interpretación sin tener que repetir este método.
     */
    public String obtenerResumen() {
        String estado;
        if (activo) {
            estado = "ACTIVO";
        } else {
            estado = "INACTIVO";
        }

        String resumen = "[" + id + "] " + ubicacion + " | " + estado + " | ";

        if (!activo) {
            return resumen + "sin evaluación";
        }
        return resumen + ultimaMedicion + " " + unidad + " -> " + evaluarMedicion();
    }

}