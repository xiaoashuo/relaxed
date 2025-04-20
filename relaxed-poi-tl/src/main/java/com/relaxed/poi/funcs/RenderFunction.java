package com.relaxed.poi.funcs;

import com.deepoove.poi.XWPFTemplate;

/**
 * Functional interface for rendering Word documents using POI-TL. This interface defines
 * a single method for rendering a Word template with custom logic.
 *
 * @param <R> the type of the render result
 * @author Yakir
 * @since 1.0
 */
@FunctionalInterface
public interface RenderFunction<R> {

	/**
	 * Renders the given Word template with custom logic.
	 * @param xwpfTemplate the Word template to render
	 * @return the result of the rendering process
	 */
	R render(XWPFTemplate xwpfTemplate);

}
