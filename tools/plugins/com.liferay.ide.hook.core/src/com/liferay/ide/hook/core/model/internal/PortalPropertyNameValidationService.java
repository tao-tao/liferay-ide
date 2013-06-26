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
 *******************************************************************************/

package com.liferay.ide.hook.core.model.internal;

import com.liferay.ide.core.ILiferayProject;
import com.liferay.ide.core.LiferayCore;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.sapphire.modeling.CapitalizationType;
import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.modeling.ModelProperty;
import org.eclipse.sapphire.modeling.Status;
import org.eclipse.sapphire.modeling.Value;
import org.eclipse.sapphire.modeling.ValueProperty;
import org.eclipse.sapphire.modeling.util.NLS;
import org.eclipse.sapphire.services.ValidationService;

/**
 * @author Gregory Amerson
 */
public class PortalPropertyNameValidationService extends ValidationService
{

    private List<String> hookPropertiesNames;
    private List<String> wildCardHookPropertiesNames;

    @Override
    protected void init()
    {
        super.init();
        final IProject project = context( IModelElement.class ).root().adapt( IFile.class ).getProject();

        final ILiferayProject liferayProject = LiferayCore.create( project );

        if( liferayProject != null )
        {
            this.hookPropertiesNames = Arrays.asList( liferayProject.getHookSupportedProperties() );
            this.wildCardHookPropertiesNames = getWildCardProperties();
        }
    }

    private boolean isValidPortalPropertyName( Value<?> value )
    {
        if( hookPropertiesNames.contains( value.getContent() ) )
        {
            return true;
        }
        else if( wildCardHookPropertiesNames != null )
        {
            for( String name : wildCardHookPropertiesNames )
            {
                if( value.getContent().toString().contains( name.subSequence( 0, ( name.indexOf( "*" ) - 1 ) ) ) ) //$NON-NLS-1$
                {
                    return true;
                }
            }
        }

        return false;
    }

    private boolean isValueEmpty( Value<?> value )
    {
        return value.getContent( false ) == null;
    }

    @Override
    public Status validate()
    {
        final Value<?> value = (Value<?>) context( IModelElement.class ).read( context( ModelProperty.class ) );
        final ValueProperty property = value.getProperty();
        final String label = property.getLabel( true, CapitalizationType.NO_CAPS, false );

        if( isValueEmpty( value ) )
        {
            final String msg = NLS.bind( Msgs.nonEmptyValueRequired, label );
            return Status.createErrorStatus( msg );
        }
        else if( !isValidPortalPropertyName( value ) )
        {
            final String msg = NLS.bind( Msgs.invalidPortalPropertyName, label );
            return Status.createErrorStatus( msg );
        }

        return Status.createOkStatus();
    }
    
    public List<String> getWildCardProperties()
    {
        List<String> properties = new ArrayList<String>();
        
        for( int i = 0; i < hookPropertiesNames.size(); i++ )
        {
            String property = hookPropertiesNames.get( i );
            
            if( property.contains( ".*" ) ) //$NON-NLS-1$
            {
                properties.add( property );
            }
        }

        return properties;
    }

    private static class Msgs extends NLS
    {
        public static String invalidPortalPropertyName;
        public static String nonEmptyValueRequired;

        static
        {
            initializeMessages( PortalPropertyNameValidationService.class.getName(), Msgs.class );
        }
    }
}
