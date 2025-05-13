package Prueba3.Modelo.GUI;

import Prueba3.Modelo.Codigo;
import Prueba3.Modelo.DAO.CodigoDAO;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

/**
 * Interfaz gráfica para la gestión de archivos PDF (Formato 023).
 * Permite subir, visualizar, buscar y eliminar archivos PDF asociados a aprendices.
 */
public class CodigoGUI2 extends JFrame {
    private JPanel panelPrincipal;
    private JButton btnSubir;
    private JPanel panelArchivos;
    private JFileChooser fileChooser;
    private CodigoDAO archivoDAO;
    private Timer timer;
    private boolean subidaHabilitada = true;
    private JTextField txtBuscarAprendiz;
    private JButton btnBuscar;
    private JPanel panelBusqueda;
    private List<Codigo> listaArchivosCompleta;

    private JLabel progressImageLabel;

    private final Color azul = Color.decode("#007AFF");
    private final Color rojo = Color.decode("#FF3B30");
    private final Color verde = Color.decode("#39A900");
    private final Color blanco = Color.decode("#FFFFFF");
    private final Color naranja = Color.decode("#F39C12");
    private final Font fuenteCalibri = new Font("Calibri", Font.PLAIN, 20);

    /**
     * Constructor de la clase CodigoGUI.
     * Inicializa los componentes y configura la ventana.
     */
    public CodigoGUI2() {
        archivoDAO = new CodigoDAO();
        listaArchivosCompleta = new ArrayList<>();

        configurarVentana();
        configurarComponentes();
        cargarArchivos();
    }

    /**
     * Configura las propiedades básicas de la ventana.
     */
    private void configurarVentana() {
        setTitle("Gestor de Archivos PDF");
        setSize(1200, 800);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
    }

    /**
     * Configura y organiza los componentes gráficos de la interfaz.
     */
    private void configurarComponentes() {
        panelPrincipal = new JPanel(new BorderLayout());

        panelArchivos = new JPanel();
        panelArchivos.setLayout(new BoxLayout(panelArchivos, BoxLayout.Y_AXIS));
        JScrollPane scrollPane = new JScrollPane(panelArchivos);
        panelPrincipal.add(scrollPane, BorderLayout.CENTER);

        panelBusqueda = new JPanel(new BorderLayout());

        ImageIcon icono = cargarImagen("C:\\Users\\Famil\\IdeaProjects\\Seguimiento\\src\\Prueba3\\Modelo\\Imagenes\\Grafico.png");
        JLabel lblImagen = new JLabel(icono);
        lblImagen.setHorizontalAlignment(JLabel.LEFT);
        JPanel panelImagen = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelImagen.add(lblImagen);
        panelBusqueda.add(panelImagen, BorderLayout.WEST);

        JPanel panelControles = new JPanel();
        panelControles.setLayout(new BoxLayout(panelControles, BoxLayout.Y_AXIS));

        JPanel panelBuscar = new JPanel(new FlowLayout(FlowLayout.LEFT));
        txtBuscarAprendiz = new JTextField(20);
        btnBuscar = new JButton("Buscar Aprendiz");
        estilizarBoton(btnBuscar, azul);
        btnBuscar.addActionListener(e -> filtrarArchivosPorAprendiz(txtBuscarAprendiz.getText().trim()));
        panelBuscar.add(new JLabel("Buscar por Cédula/Nombre:"));
        panelBuscar.add(txtBuscarAprendiz);
        panelBuscar.add(btnBuscar);

        panelControles.add(panelBuscar);
        panelBusqueda.add(panelControles, BorderLayout.CENTER);

        panelPrincipal.add(panelBusqueda, BorderLayout.NORTH);

        JPanel panelSubir = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        btnSubir = new JButton("Subir PDF");
        estilizarBoton(btnSubir, verde);
        fileChooser = new JFileChooser();
        fileChooser.setFileFilter(new FileNameExtensionFilter("Archivo PDF", "pdf"));
        btnSubir.addActionListener(e -> {
            if (subidaHabilitada) subirArchivo();
            else mostrarMensajeEspera();
        });
        panelSubir.add(btnSubir);
        panelPrincipal.add(panelSubir, BorderLayout.SOUTH);

        add(panelPrincipal);
    }

    /**
     * Aplica estilos a un botón.
     *
     * @param boton Botón a estilizar
     * @param colorFondo Color de fondo del botón
     */
    private void estilizarBoton(JButton boton, Color colorFondo) {
        boton.setBackground(colorFondo);
        boton.setForeground(blanco);
        boton.setFont(fuenteCalibri);
    }

    /**
     * Muestra un mensaje indicando que se debe esperar antes de subir otro archivo.
     */
    private void mostrarMensajeEspera() {
        JOptionPane.showMessageDialog(this,
                "Espere 20 segundos antes de subir otro archivo",
                "Espera requerida", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Maneja el proceso de subida de un archivo PDF.
     */
    private void subirArchivo() {
        if (fileChooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) return;

        File archivoSeleccionado = fileChooser.getSelectedFile();
        if (!archivoSeleccionado.getName().toLowerCase().endsWith(".pdf")) {
            JOptionPane.showMessageDialog(this, "Solo se permiten archivos PDF", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String tipoFormato = "023";
        if (tipoFormato == null) return;

        String cedulaAprendizStr = JOptionPane.showInputDialog(this,
                "Ingrese la cédula del aprendiz asignado:",
                "Cédula del Aprendiz", JOptionPane.QUESTION_MESSAGE);
        if (cedulaAprendizStr == null || cedulaAprendizStr.trim().isEmpty()) {
            JOptionPane.showMessageDialog(this, "Debe ingresar la cédula del aprendiz", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        try {
            int cedulaAprendiz = Integer.parseInt(cedulaAprendizStr);
            Map<String, String> infoAprendiz = archivoDAO.obtenerInfoCompletaAprendizPorCedula(cedulaAprendiz);

            if (infoAprendiz.get("id") == null) {
                JOptionPane.showMessageDialog(this, "No se encontró un aprendiz con esa cédula", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            int idAprendiz = Integer.parseInt(infoAprendiz.get("id"));
            String nombreAprendiz = infoAprendiz.get("nombre");
            String numeroDocumento = infoAprendiz.get("cedula");

            String observaciones = JOptionPane.showInputDialog(this,
                    "Ingrese las observaciones (opcional):",
                    "Observaciones", JOptionPane.QUESTION_MESSAGE);
            observaciones = observaciones == null ? "" : observaciones;

            File destino = guardarArchivoLocalmente(archivoSeleccionado);
            if (destino == null) return;

            Codigo archivo = crearObjetoArchivo(
                    archivoSeleccionado, destino, tipoFormato, observaciones,
                    nombreAprendiz, numeroDocumento, idAprendiz
            );

            if (archivoDAO.insertar(archivo)) {
                JOptionPane.showMessageDialog(this, "Archivo subido con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
                cargarArchivos();
                deshabilitarSubidaTemporalmente();
            } else {
                JOptionPane.showMessageDialog(this, "Error al guardar en la base de datos", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "La cédula debe ser un número válido", "Error", JOptionPane.ERROR_MESSAGE);
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Guarda una copia local del archivo PDF subido.
     *
     * @param archivo Archivo a guardar localmente
     * @return File del archivo guardado o null si falla
     */
    private File guardarArchivoLocalmente(File archivo) {
        File directorio = new File("pdf_almacenados");
        if (!directorio.exists() && !directorio.mkdir()) {
            JOptionPane.showMessageDialog(this, "No se pudo crear el directorio para guardar archivos",
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }

        File destino = new File(directorio, archivo.getName());
        try {
            Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
            return destino;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Error al copiar el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Crea un objeto Codigo a partir de un archivo PDF.
     *
     * @param archivoSeleccionado Archivo PDF seleccionado
     * @param destino Archivo guardado localmente
     * @param tipoFormato Tipo de formato del archivo
     * @param observaciones Observaciones sobre el archivo
     * @param nombreAprendiz Nombre del aprendiz asociado
     * @param cedulaAprendiz Cédula del aprendiz asociado
     * @param idAprendiz ID del aprendiz en la base de datos
     * @return Objeto Codigo creado
     * @throws IOException Si ocurre un error al leer el archivo
     */
    private Codigo crearObjetoArchivo(File archivoSeleccionado, File destino, String tipoFormato,
                                      String observaciones, String nombreAprendiz,
                                      String cedulaAprendiz, int idAprendiz) throws IOException {
        Codigo archivo = new Codigo();
        archivo.setNombreArchivo(archivoSeleccionado.getName());
        archivo.setRutaArchivo(destino.getAbsolutePath());

        if (archivoSeleccionado.length() < 10_000_000) {
            archivo.setArchivo(Files.readAllBytes(destino.toPath()));
        } else {
            archivo.setArchivo(("RUTA:" + destino.getAbsolutePath()).getBytes());
        }

        archivo.setTipoFormato(tipoFormato);
        archivo.setObservaciones(observaciones);
        archivo.setFecha(new Date());
        archivo.setNombreAprendiz(nombreAprendiz);
        archivo.setCedulaAprendiz(cedulaAprendiz);
        archivo.setIdAprendiz(idAprendiz);
        archivo.setIdUsuario(obtenerIdUsuarioActual());
        return archivo;
    }

    /**
     * Deshabilita temporalmente el botón de subir archivos.
     */
    private void deshabilitarSubidaTemporalmente() {
        subidaHabilitada = false;
        btnSubir.setEnabled(false);
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                subidaHabilitada = true;
                btnSubir.setEnabled(true);
                timer.cancel();
            }
        }, 20000);
    }

    /**
     * Obtiene el ID del usuario actual (simulado).
     *
     * @return ID del usuario actual (valor temporal)
     */
    private int obtenerIdUsuarioActual() {
        return 1;
    }

    /**
     * Carga los archivos desde la base de datos y los muestra en la interfaz.
     */
    private void cargarArchivos() {
        listaArchivosCompleta = archivoDAO.listarTodos();
        mostrarArchivos(listaArchivosCompleta);
    }

    /**
     * Muestra la lista de archivos en el panel principal.
     *
     * @param archivos Lista de archivos a mostrar
     */
    private void mostrarArchivos(List<Codigo> archivos) {
        panelArchivos.removeAll();
        for (Codigo archivo : archivos) {
            agregarArchivoAPanel(archivo);
        }
        panelArchivos.revalidate();
        panelArchivos.repaint();
    }

    /**
     * Agrega un archivo al panel de visualización.
     *
     * @param archivo Archivo a agregar al panel
     */
    private void agregarArchivoAPanel(final Codigo archivo) {
        JPanel panelArchivo = new JPanel(new BorderLayout());
        panelArchivo.setBorder(BorderFactory.createEtchedBorder());
        panelArchivo.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

        JLabel lblNombre = new JLabel("Nombre: " + archivo.getNombreArchivo());
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        JLabel lblFecha = new JLabel("Subido el: " + sdf.format(archivo.getFecha()));
        JLabel lblAprendiz = new JLabel("Aprendiz: " + archivo.getNombreAprendiz() +
                " (Cédula: " + archivo.getCedulaAprendiz() + ")");
        JLabel lblObservacion = new JLabel("Observaciones: " + archivo.getObservaciones());

        JPanel panelInfo = new JPanel(new GridLayout(4, 1));
        panelInfo.add(lblNombre);
        panelInfo.add(lblFecha);
        panelInfo.add(lblAprendiz);
        panelInfo.add(lblObservacion);

        JButton btnVer = new JButton("Previsualizar");
        estilizarBoton(btnVer, naranja);
        btnVer.addActionListener(e -> previsualizarArchivo(archivo));

        JButton btnEliminar = new JButton("Eliminar");
        estilizarBoton(btnEliminar, rojo);
        btnEliminar.addActionListener(e -> eliminarArchivo(archivo, panelArchivo));

        JPanel panelBotones = new JPanel(new GridLayout(2, 1));
        panelBotones.add(btnVer);
        panelBotones.add(btnEliminar);

        panelArchivo.add(panelInfo, BorderLayout.CENTER);
        panelArchivo.add(panelBotones, BorderLayout.EAST);

        panelArchivos.add(panelArchivo);
    }

    /**
     * Abre un archivo PDF para su previsualización.
     *
     * @param archivo Archivo a previsualizar
     */
    private void previsualizarArchivo(Codigo archivo) {
        try {
            File archivoParaAbrir = new File(archivo.getRutaArchivo());
            if (archivoParaAbrir.exists()) {
                Desktop.getDesktop().open(archivoParaAbrir);
            } else {
                JOptionPane.showMessageDialog(this,
                        "El archivo no se encuentra en la ruta especificada",
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    "Error al abrir el archivo: " + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * Elimina un archivo de la base de datos y del sistema de archivos.
     *
     * @param archivo Archivo a eliminar
     * @param panelArchivo Panel del archivo a remover de la interfaz
     */
    private void eliminarArchivo(Codigo archivo, JPanel panelArchivo) {
        int confirmacion = JOptionPane.showConfirmDialog(this,
                "¿Está seguro de eliminar este archivo?", "Confirmar eliminación",
                JOptionPane.YES_NO_OPTION);
        if (confirmacion != JOptionPane.YES_OPTION) return;

        if (archivoDAO.eliminar(archivo.getIdSeguimiento())) {
            if (archivo.getRutaArchivo() != null) {
                new File(archivo.getRutaArchivo()).delete();
            }
            panelArchivos.remove(panelArchivo);
            panelArchivos.revalidate();
            panelArchivos.repaint();
        }
    }

    /**
     * Filtra los archivos por cédula o nombre de aprendiz.
     *
     * @param busqueda Texto de búsqueda (cédula o nombre)
     */
    private void filtrarArchivosPorAprendiz(String busqueda) {
        if (busqueda.isEmpty()) {
            mostrarArchivos(listaArchivosCompleta);
            return;
        }

        List<Codigo> archivosFiltrados = new ArrayList<>();
        for (Codigo archivo : listaArchivosCompleta) {
            if (archivo.getCedulaAprendiz().contains(busqueda) ||
                    archivo.getNombreAprendiz().toLowerCase().contains(busqueda.toLowerCase())) {
                archivosFiltrados.add(archivo);
            }
        }
        mostrarArchivos(archivosFiltrados);
    }

    /**
     * Carga una imagen desde una ruta especificada.
     *
     * @param ruta Ruta del archivo de imagen
     * @return ImageIcon con la imagen cargada o null si falla
     */
    private ImageIcon cargarImagen(String ruta) {
        try {
            ImageIcon icono = new ImageIcon(ruta);
            Image imagen = icono.getImage().getScaledInstance(600, 400, Image.SCALE_SMOOTH);
            return new ImageIcon(imagen);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "No se pudo cargar la imagen: " + e.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    /**
     * Método principal para ejecutar la aplicación.
     *
     * @param args Argumentos de línea de comandos
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CodigoGUI2().setVisible(true));
    }
}