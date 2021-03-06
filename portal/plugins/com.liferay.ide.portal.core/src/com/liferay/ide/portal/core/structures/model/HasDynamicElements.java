/*******************************************************************************
 * Copyright (c) 2000-2013 Liferay, Inc. All rights reserved.
 *
 * This library is free software; you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License as published by the Free
 * Software Foundation; either version 2.1 of the License, or (at your option)
 * any later version.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 *
 * Contributors:
 *      Gregory Amerson - initial implementation and ongoing maintenance
 *******************************************************************************/
package com.liferay.ide.portal.core.structures.model;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.ListProperty;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Type;
import org.eclipse.sapphire.modeling.xml.annotations.XmlListBinding;


/**
 * @author Gregory Amerson
 */
public interface HasDynamicElements extends Element
{

    ElementType TYPE = new ElementType( HasDynamicElements.class );

    // *** DynamicElements ***

    @Type( base = DynamicElement.class )
    @Label( standard = "dynamic elements" )
    @XmlListBinding
    (
        mappings =
        {
            @XmlListBinding.Mapping
            (
                element = "dynamic-element",
                type = DynamicElement.class
            )
        }
    )
    ListProperty PROP_DYNAMIC_ELEMENTS = new ListProperty( TYPE, "DynamicElements" ); //$NON-NLS-1$

    ElementList<DynamicElement> getDynamicElements();

}
