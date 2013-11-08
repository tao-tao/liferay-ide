package com.liferay.ide.project.core.model;

import com.liferay.ide.project.core.model.internal.ProfileIdPossibleValuesService;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.NoDuplicates;
import org.eclipse.sapphire.modeling.annotations.PossibleValues;
import org.eclipse.sapphire.modeling.annotations.Service;


public interface Profile extends Element
{
    ElementType TYPE = new ElementType( Profile.class );

    // *** Id ***

    @Label( standard = "id" )
    @PossibleValues( values = { "foo", "bar" } )
    @NoDuplicates
    @Service(impl = ProfileIdPossibleValuesService.class)
    ValueProperty PROP_ID = new ValueProperty( TYPE, "Id" );

    Value<String> getId();

    void setId( String value );


}
