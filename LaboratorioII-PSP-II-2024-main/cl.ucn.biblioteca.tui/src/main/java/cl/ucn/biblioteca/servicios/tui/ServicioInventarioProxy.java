package cl.ucn.biblioteca.servicios.tui;

import cl.ucn.biblioteca.servicio.api.ServicioInventarioLibro;

import org.apache.felix.service.command.Descriptor;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

import cl.ucn.biblioteca.api.ExcepcionLibroExistente;
import cl.ucn.biblioteca.api.ExcepcionLibroInvalido;


public class ServicioInventarioProxy {

	public static final String AMBITO = "libro";
	public static final String[] FUNCIONES = new String[] {"buscar", "ingresar"};
	private BundleContext contexto;
	public ServicioInventarioProxy(BundleContext contexto)
	{
		this.contexto = contexto;
	}

	@Descriptor("Ingresar libro")
	public String ingresar(@Descriptor("ISBN") String isbn,
						   @Descriptor("Titulo") String titulo,
						   @Descriptor("Autor") String autor,
						   @Descriptor("Categoria") String categoria)
			throws ExcepcionLibroExistente, ExcepcionLibroInvalido
	{
		ServicioInventarioLibro servicio = obtenerServicio();
		servicio.ingresar(isbn, titulo, autor, categoria);
		return servicio.ingresar(isbn, titulo, autor, categoria);
	}

	@Descriptor("Remover libro")
	public void remover(@Descriptor("ISBN") String isbn)
	{
		ServicioInventarioLibro servicio = obtenerServicio();
		servicio.remover(isbn);
	}

	@Descriptor("Modificar categoria")
	public void modificarCategoria(@Descriptor("ISBN") String isbn,
								   @Descriptor("Categoria") String categoria)
	{
		ServicioInventarioLibro servicio = obtenerServicio();
		servicio.modificarCategoria(isbn, categoria);
	}

	@Descriptor("Obtener libro")
	public String obtener(@Descriptor("ISBN") String isbn)
	{
		ServicioInventarioLibro servicio = obtenerServicio();
		//return servicio.obtener(isbn).toString();
		return "sos";
	}

	protected ServicioInventarioLibro obtenerServicio() {

		ServiceReference referencia = contexto.getServiceReference(ServicioInventarioLibro.class.getName());
		if (referencia == null)
			throw new RuntimeException("ServicioInventarioLibro no esta registrado, no puedo invocar operacion");

		ServicioInventarioLibro servicio = (ServicioInventarioLibro) this.contexto.getService(referencia);
		if (servicio == null)
			throw new RuntimeException("ServicioInventarioLibro no esta registrado, no puedo invocar operacion");

		return servicio;
	}

}
