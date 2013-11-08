package com.liferay.ide.project.core.model;

import com.liferay.ide.project.core.model.internal.RuntimeNameDefaultValueService;
import com.liferay.ide.project.core.model.internal.RuntimeNamePossibleValuesService;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.annotations.Services;


public interface HasLiferayRuntime extends Element
{
    ElementType TYPE = new ElementType( HasLiferayRuntime.class );

    // *** RuntimeName ***

    @Label( standard = "runtime" )
    @Services
    (
        value =
        {
            @Service( impl = RuntimeNamePossibleValuesService.class ),
            @Service( impl = RuntimeNameDefaultValueService.class ),
        }
    )
    ValueProperty PROP_RUNTIME_NAME = new ValueProperty( TYPE, "RuntimeName" ); //$NON-NLS-1$

    Value<String> getRuntimeName();

    void setRuntimeName( String value );
}
