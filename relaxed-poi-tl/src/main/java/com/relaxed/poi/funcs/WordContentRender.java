package com.relaxed.poi.funcs;

import com.deepoove.poi.config.Configure;
import com.relaxed.poi.domain.LabelData;

/**
 * Interface for rendering content in Word documents. This interface defines the contract
 * for content renderers that process different types of data for Word document
 * generation.
 *
 * @param <T> the type of label data this renderer can process
 * @author Yakir
 * @since 1.0.0
 */
public interface WordContentRender<T extends LabelData> {

	/**
	 * Returns the content type that this renderer supports.
	 * @return the content type identifier
	 */
	String contentType();

	/**
	 * Renders the given data into a format suitable for Word document generation.
	 * @param configure the configuration object for rendering
	 * @param data the data to be rendered
	 * @return the rendered value object
	 */
	Object render(Configure configure, T data);

}
