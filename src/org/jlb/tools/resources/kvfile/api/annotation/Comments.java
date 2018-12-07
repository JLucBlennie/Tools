package org.jlb.tools.resources.kvfile.api.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Commentaires sur une entrée de fichier de propriétés ou sur un fichier de
 * propriétés.
 * 
 * @author JLuc
 * 
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ ElementType.FIELD })
public @interface Comments {

    /**
     * Retourn la valeur.
     * 
     * @return la valeur
     */
    String value();
}
