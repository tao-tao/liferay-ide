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
package com.liferay.ide.core;



/**
 * @author Gregory Amerson
 */
public abstract class AbstractLiferayProjectProvider implements ILiferayProjectProvider, Comparable<ILiferayProjectProvider>
{
    private Class<?>[] classTypes;
    private String displayName;
    private boolean isDefault;
    private int priority;
    private String shortName;

    public AbstractLiferayProjectProvider( Class<?>[] types )
    {
        this.classTypes = types;
    }

    public int compareTo( ILiferayProjectProvider provider )
    {
        if( provider != null )
        {
            return this.shortName.compareTo( provider.getShortName() );
        }

        return 0;
    }

    public String getDisplayName()
    {
        return this.displayName;
    }

    public String[] getPossibleVersions( Object... args )
    {
        return new String[] {};
    }

    public int getPriority()
    {
        return this.priority;
    }

    public String getShortName()
    {
        return this.shortName;
    }

    public boolean isDefault()
    {
        return this.isDefault;
    }

    public boolean provides( Class<?> type )
    {
        if( type != null )
        {
            for( Class<?> classType : classTypes )
            {
                if( classType.isAssignableFrom( type ) )
                {
                    return true;
                }
            }
        }

        return false;
    }

    public void setDefault( boolean isDefault )
    {
        this.isDefault = isDefault;
    }

    public void setDisplayName( String displayName )
    {
        this.displayName = displayName;
    }

    public void setPriority( int priority )
    {
        this.priority = priority;
    }

    public void setShortName( String shortName )
    {
        this.shortName = shortName;
    }
}
