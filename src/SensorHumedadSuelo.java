/**
 * Sensor que mide la humedad del suelo en porcentaje.
 * Su medición se usa para decidir si es necesario regar.
 */
public class SensorHumedadSuelo extends Sensor {

    private static final double LIMITE_BAJO = 30.0;
    private static final double LIMITE_ALTO = 70.0;

    public SensorHumedadSuelo(String id, String ubicacion) {
        super(id, ubicacion, "%");
    }

    /**
     * Indica si el suelo necesita riego: el sensor debe estar activo
     * y su medición debe ser baja. Usa el mismo límite que evaluarMedicion(),
     * así el criterio existe en un solo lugar.
     */
    public boolean requiereRiego() {
        return isActivo() && getUltimaMedicion() < LIMITE_BAJO;
    }

    /**
     * Rechaza valores fuera de 0 a 100 %. Si el valor es válido,
     * la superclase se encarga de registrarlo.
     */
    @Override
    public boolean registrarMedicion(double valor) {
        if (valor < 0 || valor > 100) {
            return false;
        }
        return super.registrarMedicion(valor);
    }

    @Override
    public String evaluarMedicion() {
        double medicion = getUltimaMedicion();

        if (medicion < LIMITE_BAJO) {
            return "Humedad del suelo baja";
        } else if (medicion > LIMITE_ALTO) {
            return "Humedad del suelo alta";
        } else {
            return "Humedad del suelo adecuada";
        }
    }

}