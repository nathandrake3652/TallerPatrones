package cl.ucn.biblioteca.servicio.api;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import cl.ucn.biblioteca.api.*;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;

public class ServicioInventarioLibroImpl implements ServicioInventarioLibro{


	private BundleContext contexto;

	public ServicioInventarioLibroImpl(BundleContext contexto) {
		
		this.contexto = contexto;
	}

	@Override
	public String ingresar(String isbn, String titulo, String autor, String categoria) {
		Inventario inventario = buscarLibroEnInventario();
		try {
			LibroMutable libro = inventario.crearLibro(isbn);
			libro.setTitulo(titulo);
			libro.setAutor(autor);
			libro.setCategoria(categoria);
			inventario.guardarLibro(libro);
			return isbn;
		} catch (ExcepcionLibroInvalido e) {
			e.printStackTrace();
		} catch (ExcepcionLibroExistente e) {
            throw new RuntimeException(e);
        }
        return null;



	}

	@Override
	public void remover(String isbn) {
		Inventario inventario = buscarLibroEnInventario();
		try {

			inventario.removerLibro(isbn);
			System.out.print("Libro removido");
		} catch (ExcepcionLibroNoEncontrado e) {
			e.printStackTrace();
	}

	}

	@Override
	public void modificarCategoria(String isbn, String categoria) {
		Inventario inventario = buscarLibroEnInventario();
		try {
			LibroMutable libro = inventario.cargarLibroParaEdicion(isbn);
			System.out.print("Libro encontrado, la categoria de este libro es " + libro.getCategoria() + "\n");
			libro.setCategoria(categoria);
			inventario.guardarLibro(libro);
			System.out.print("Categoria modificada con exito la nueva categoria es" + libro.getCategoria() + "\n");
		} catch (ExcepcionLibroNoEncontrado e) {
			e.printStackTrace();
		} catch (ExcepcionLibroInvalido e) {
			e.printStackTrace();
		}
		
	}

	@Override
	public Libro obtener(String titulo) throws ExcepcionLibroNoEncontrado {
		Inventario inventario = buscarLibroEnInventario();
		Set <String> isbns = inventario.buscarLibros(Map.of(Inventario.CriterioBusqueda.TITULO_LIKE, titulo));
		if (isbns.isEmpty()) {
			throw new ExcepcionLibroNoEncontrado("No se encontro el libro con titulo: " + titulo);
		}
		for(String isbn : isbns) {
			String aux = inventario.cargarLibro(isbn).getTitulo();
			if(aux.equals(titulo)) {
				return inventario.cargarLibro(isbn);
			}
		}
		throw new ExcepcionLibroNoEncontrado("No se encontro el libro con titulo: " + titulo);
	}

	private Inventario buscarLibroEnInventario()  {
		
		String nombre = Inventario.class.getName();
		ServiceReference ref = this.contexto.getServiceReference(nombre);
		return (Inventario) this.contexto.getService(ref);
	}
}
