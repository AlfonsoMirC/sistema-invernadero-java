# Análisis y diseño

## 1. Descripción del problema

Voy a representar, mediante una aplicación de consola en Java, un **invernadero inteligente simplificado**. El sistema observa tres variables ambientales (temperatura del aire, humedad ambiental y humedad del suelo) por medio de sensores, interpreta cada medición según el tipo de variable y, a partir de la humedad del suelo, decide si el sistema de riego debe activarse o quedarse inactivo.

El funcionamiento general sigue tres pasos:

OBSERVAR  →  EVALUAR  →  ACTUAR
(sensores)   (cada sensor     (sistema de riego)
              interpreta su
              medición)


### Información que el sistema necesita manejar

- Datos de cada sensor: una forma de identificarlo, el lugar donde está instalado (por ejemplo, "zona norte") y si está activo o inactivo.
- La última medición de cada sensor. Las mediciones son **simuladas** desde el programa, no vienen de sensores físicos.
- La unidad de cada medición: °C para la temperatura y % para las humedades.
- Los criterios para interpretar cada medición, que son **distintos para cada variable**:

| Variable | Unidad | Baja | Adecuada | Alta |
|---|---|---|---|---|
| Temperatura del aire | °C | menor a 18 | 18 a 30 | mayor a 30 |
| Humedad ambiental | % | menor a 40 | 40 a 70 | mayor a 70 |
| Humedad del suelo | % | menor a 30 | 30 a 70 | mayor a 70 |

- El estado del sistema de riego: activo (suministrando agua) o inactivo (riego detenido).

### Elementos que intervienen

- El invernadero, como el espacio que agrupa a todos los demás elementos.
- Tres tipos de sensores: de temperatura, de humedad ambiental y de humedad del suelo.
- El sistema de riego.

### Operaciones generales

1. Registrar (simular) una medición en cada sensor.
2. Consultar la información de los sensores.
3. Interpretar cada medición de acuerdo con el tipo de sensor.
4. Decidir, con base en la humedad del suelo, si es necesario regar.
5. Activar, desactivar y consultar el estado del sistema de riego.
6. Mostrar en consola el estado general del invernadero.

### Observación importante

Un número por sí solo no dice nada. El valor `32` significa cosas diferentes dependiendo de quién lo mide:


Sensor de temperatura       → 32 °C → temperatura alta
Sensor de humedad ambiental → 32 %  → humedad ambiental baja
Sensor de humedad del suelo → 32 %  → humedad del suelo adecuada


Por eso la interpretación de una medición tiene que estar ligada al tipo de sensor que la produce, y no a un lugar central que reciba solo el número.

---

## 2. Identificación de objetos

### Sensor (concepto general)

- **Qué representa:** cualquier dispositivo del invernadero que observa una variable ambiental.
- **Por qué debe existir como objeto:** tiene información propia (identificación, ubicación, estado y última medición) y comportamientos propios (registrar una medición, informarla e interpretarla). Además, es el concepto que agrupa lo que tienen en común todos los sensores.
- **Responsabilidad:** observar una variable y conservar su última medición.

> En el invernadero no existe un "sensor genérico" físico; todos son de algún tipo concreto. Aun así, el concepto general es útil para no repetir lo que comparten.

### Sensor de temperatura

- **Qué representa:** el sensor que mide la temperatura del aire dentro del invernadero.
- **Por qué debe existir como objeto:** es un sensor, pero mide en °C e interpreta sus mediciones con sus propios rangos.
- **Responsabilidad:** registrar la temperatura del aire e indicar si es baja, adecuada o alta.

### Sensor de humedad ambiental

- **Qué representa:** el sensor que mide la humedad presente en el aire.
- **Por qué debe existir como objeto:** es un sensor con unidad % y criterios de interpretación propios.
- **Responsabilidad:** registrar la humedad ambiental e indicar si es baja, adecuada o alta.

### Sensor de humedad del suelo

- **Qué representa:** el sensor que estima qué tan húmedo o seco está el suelo de las plantas.
- **Por qué debe existir como objeto:** es un sensor con criterios propios y, además, su medición es la que se usa para decidir el riego.
- **Responsabilidad:** registrar la humedad del suelo, interpretarla e indicar si el suelo necesita riego.

### Sistema de riego

- **Qué representa:** el mecanismo que suministra agua a las plantas.
- **Por qué debe existir como objeto:** tiene un estado propio (activo o inactivo) que cambia durante la ejecución y acciones propias (activarse y desactivarse).
- **Responsabilidad:** ejecutar o detener el riego. No mide ni interpreta nada; solamente actúa.

### Invernadero

- **Qué representa:** el espacio que contiene a los sensores y al sistema de riego.
- **Por qué debe existir como objeto:** alguien tiene que coordinar a los demás elementos: pedir a los sensores que evalúen sus mediciones y, con la información del sensor de humedad del suelo, decidir si se activa el riego. Si no existiera, esa lógica terminaría mezclada en `Main` o dentro de los sensores o del sistema de riego, y ninguno de ellos debería tener esa responsabilidad.
- **Responsabilidad:** agrupar los sensores y el sistema de riego y coordinar la decisión de riego.

### Elementos que decidí no representar como objetos (por ahora)

| Elemento | Motivo |
|---|---|
| Medición | Un valor numérico es suficiente; su significado lo da el sensor que la produce. Podría convertirse en objeto si después se necesitara guardar fecha, hora o un historial. |
| Interpretación (baja/adecuada/alta) | Es el resultado que devuelve el sensor, no algo con comportamiento propio. |
| Rangos de interpretación | Son datos fijos de cada tipo de sensor. |
| Plantas, agua, bomba, tuberías | Quedan fuera del alcance; el documento de contexto indica que no son necesarios. |

---

## 3. Estado y comportamiento

| Objeto propuesto | Responsabilidad | Información que debe conservar | Comportamientos que debe realizar |
|---|---|---|---|
| Sensor (general) | Observar una variable y conservar su última medición | Identificación, ubicación, si está activo o inactivo, última medición | Permitir conocer su identificación, ubicación y estado. Activarse y desactivarse. Registrar una nueva medición simulada. Permitir conocer su última medición. Interpretar su medición. Mostrar su información. |
| Sensor de temperatura | Observar la temperatura del aire | Lo mismo que cualquier sensor, más su unidad (°C) y sus rangos (18 y 30 °C) | Interpretar su medición como temperatura baja, adecuada o alta. Indicar su unidad. Podría expresar la temperatura en otra escala, por ejemplo °F. |
| Sensor de humedad ambiental | Observar la humedad del aire | Lo mismo que cualquier sensor, más su unidad (%) y sus rangos (40 y 70 %) | Interpretar su medición como humedad ambiental baja, adecuada o alta. Indicar su unidad. |
| Sensor de humedad del suelo | Observar la humedad del suelo | Lo mismo que cualquier sensor, más su unidad (%) y sus rangos (30 y 70 %) | Interpretar su medición como humedad del suelo baja, adecuada o alta. Indicar su unidad. Indicar si el suelo necesita riego. |
| Sistema de riego | Ejecutar o detener el suministro de agua | Identificación o zona donde riega, estado (activo o inactivo) | Activarse. Desactivarse. Permitir consultar su estado. Mostrar su estado. |
| Invernadero | Agrupar y coordinar los elementos del sistema | Nombre, conjunto de sensores, sistema de riego | Agregar sensores. Pedir a cada sensor que interprete su medición. Decidir, con la información del sensor de humedad del suelo, si el riego se activa o se desactiva. Mostrar un resumen del estado general. |

### Supuestos que tomé

- Un sensor inactivo no debería registrar mediciones nuevas ni tomarse en cuenta para decidir el riego.
- Las mediciones de humedad solo tienen sentido entre 0 % y 100 %.
- El sistema de riego no decide por sí mismo cuándo regar; recibe la indicación de activarse o desactivarse.

---

## 4. Características comunes y especialización

### 1. ¿Qué información tienen en común?

Los tres sensores tienen una identificación, una ubicación, un estado (activo o inactivo) y una última medición. Todos tienen además una unidad, aunque el valor de esa unidad no sea el mismo.

### 2. ¿Qué comportamientos tienen en común?

Todos pueden registrar una medición, permitir consultarla, activarse o desactivarse, mostrar su información e **interpretar su medición**.

### 3. ¿Qué características cambian dependiendo del tipo de sensor?

- La unidad (°C o %).
- Los rangos con los que se interpreta la medición.
- El resultado de la interpretación ("temperatura alta" no es lo mismo que "humedad del suelo baja").
- Algunos comportamientos que solo tienen sentido para un tipo, por ejemplo indicar si el suelo necesita riego (solo humedad del suelo) o expresar la temperatura en °F (solo temperatura).

Lo más importante que noté es que **interpretar la medición es un comportamiento común, pero la forma de hacerlo cambia en cada tipo de sensor**. Todos lo hacen, pero cada uno a su manera.

| Característica | Temperatura | Humedad ambiental | Humedad del suelo | ¿Común o particular? |
|---|---|---|---|---|
| Identificación | Sí | Sí | Sí | Común |
| Ubicación | Sí | Sí | Sí | Común |
| Activo / inactivo | Sí | Sí | Sí | Común |
| Última medición | Sí | Sí | Sí | Común |
| Interpretar la medición | Sí | Sí | Sí | Común, pero cada uno lo hace distinto |
| Unidad | °C | % | % | Particular |
| Rangos | 18 / 30 | 40 / 70 | 30 / 70 | Particular |
| Indicar si requiere riego | No | No | Sí | Particular |

### 4. ¿Existe un concepto general que permita representar a todos los sensores?

Sí: el **sensor**. Concentra la información y los comportamientos que comparten los tres tipos.

### 5. ¿Qué elementos podrían representar especializaciones de ese concepto?

El sensor de temperatura, el sensor de humedad ambiental y el sensor de humedad del suelo. Cada uno **es un** sensor, pero agrega su propia unidad, sus propios rangos y su propia forma de interpretar.


                  Sensor
                    │
      ┌─────────────┼─────────────┐
      │             │             │
 Temperatura   Hum. ambiental  Hum. suelo
          (lo que es particular)


### Una alternativa que consideré

Las dos humedades usan la misma unidad (%), así que pensé en un concepto intermedio "sensor de humedad" del que salieran ambas. Lo descarté porque lo único que comparten es la unidad: sus rangos y su significado son distintos. Tampoco tiene sentido decir que el sensor de humedad del suelo **es un** sensor de humedad ambiental; son tipos hermanos, no uno especialización del otro. Por eso los tres tipos se especializan directamente del sensor general.

---

## 5. Relaciones entre objetos

### Relaciones "ES UN"

- Un sensor de temperatura **es un** sensor.
- Un sensor de humedad ambiental **es un** sensor.
- Un sensor de humedad del suelo **es un** sensor.

Estas relaciones indican generalización y especialización, por lo que **sí se representarán con herencia**.

### Relaciones "TIENE / UTILIZA UN"

- El invernadero **tiene** varios sensores.
- El invernadero **tiene** un sistema de riego.
- El invernadero **utiliza** la información del sensor de humedad del suelo para decidir si el sistema de riego se activa.

Estas relaciones son de colaboración, **no de herencia**.

### Colaboración entre objetos

| Objeto que necesita algo | De quién | Qué necesita | Para qué |
|---|---|---|---|
| Invernadero | Cada sensor | La interpretación de su medición | Mostrar el estado general |
| Invernadero | Sensor de humedad del suelo | Saber si la humedad del suelo es baja | Decidir si se riega |
| Sistema de riego | Invernadero | La indicación de activarse o desactivarse | Cambiar su estado |
| Invernadero | Sistema de riego | Su estado actual | Mostrarlo en consola |

### ¿Cuáles relaciones se representan con herencia y cuáles no?

| Relación | ¿Herencia? | Motivo |
|---|---|---|
| Sensor de temperatura → Sensor | Sí | Es un sensor |
| Sensor de humedad ambiental → Sensor | Sí | Es un sensor |
| Sensor de humedad del suelo → Sensor | Sí | Es un sensor |
| Invernadero → Sensor | No | El invernadero contiene sensores, no es uno |
| Invernadero → Sistema de riego | No | El invernadero contiene un sistema de riego, no es uno |
| Sistema de riego → Sensor | No | El sistema de riego actúa; no observa (ver la pregunta final) |

### Responsabilidades que no deben duplicarse

- **Interpretar una medición** le corresponde solo al sensor que la produce. Ni el invernadero ni el sistema de riego deben repetir los rangos. Si, por ejemplo, el sistema de riego comparara por su cuenta si la humedad es menor a 30 %, el criterio quedaría escrito en dos lugares y habría que cambiarlo en ambos si se modificara.
- **La información común** (identificación, ubicación, estado y última medición) debe existir una sola vez en el concepto general de sensor, no repetida en cada tipo.
- **El estado del riego** solo lo conserva y modifica el sistema de riego. El invernadero le pide que se active o desactive, pero no cambia ese estado directamente.
- **La decisión de regar** se toma en un solo lugar: el invernadero.

### ¿Por qué `SistemaRiego` no debería ser una subclase de `Sensor`?

Porque sus responsabilidades son diferentes, incluso opuestas:

1. **Un sensor observa; el sistema de riego actúa.** El sensor obtiene información del entorno, y el sistema de riego modifica el entorno suministrando agua. Corresponden a pasos distintos del ciclo observar → evaluar → actuar.
2. **No se cumple la relación "es un".** Un sistema de riego no es un sensor; solamente usa (de forma indirecta) la información que un sensor proporciona.
3. **Heredaría cosas que no tienen sentido para él.** Tendría una "última medición", una unidad y un comportamiento para "interpretar su medición", pero no mide nada, así que no podría cumplirlos de forma coherente.
4. **Compartir algunos datos no justifica la herencia.** Es cierto que ambos tienen una identificación y un estado activo/inactivo, pero esa coincidencia no los convierte en el mismo tipo de cosa. La herencia debe venir de una relación conceptual, no de querer reutilizar un par de atributos.
5. **Rompería el trato común a los sensores.** Si fuera un sensor, podría guardarse en la misma colección que los demás, y al pedirle a cada elemento que interprete su medición, el sistema de riego no tendría nada que interpretar.

La relación correcta es de **colaboración**: el invernadero consulta al sensor de humedad del suelo y, con esa información, le indica al sistema de riego si debe activarse o desactivarse.