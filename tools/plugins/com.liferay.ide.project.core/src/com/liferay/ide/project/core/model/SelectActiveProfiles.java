package com.liferay.ide.project.core.model;

import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.ListProperty;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Type;


public interface SelectActiveProfiles extends Element
{

    ElementType TYPE = new ElementType( SelectActiveProfiles.class );

    // *** SelectedProfiles ***

    @Type( base = Profile.class )
    @Label( standard = "selected profiles" )
    ListProperty PROP_SELECTED_PROFILES = new ListProperty( TYPE, "SelectedProfiles" );

    ElementList<Profile> getSelectedProfiles();
}
