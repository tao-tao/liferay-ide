package com.liferay.ide.project.core.model;

import com.liferay.ide.core.ILiferayProjectProvider;
import com.liferay.ide.project.core.model.internal.VersionPossibleValuesService;

import org.eclipse.sapphire.ElementType;
import org.eclipse.sapphire.Value;
import org.eclipse.sapphire.ValueProperty;
import org.eclipse.sapphire.modeling.annotations.DefaultValue;
import org.eclipse.sapphire.modeling.annotations.Label;
import org.eclipse.sapphire.modeling.annotations.Service;
import org.eclipse.sapphire.modeling.annotations.Type;


public interface NewLiferayProfile extends HasLiferayRuntime
{

    ElementType TYPE = new ElementType( NewLiferayProfile.class );


    // *** ProfileId ***

    @Label( standard = "profile id" )
    @DefaultValue( text = "${ RuntimeName }" )
    ValueProperty PROP_PROFILE_ID = new ValueProperty( TYPE, "ProfileId" );


    Value<String> getProfileId();
    void setProfileId( String value );


    // *** LiferayMavenPluginVersion ***

    @Label( standard = "liferay maven plugin version" )
    @DefaultValue( text = "6.2.0-RC5" )
    @Service( impl = VersionPossibleValuesService.class )
    ValueProperty PROP_LIFERAY_MAVEN_PLUGIN_VERSION = new ValueProperty( TYPE, "LiferayMavenPluginVersion" );

    Value<String> getLiferayMavenPluginVersion();
    void setLiferayMavenPluginVersion( String value );


    // *** AddToParentPom ***

    @Type( base = Boolean.class )
    @Label( standard = "add to parent pom" )
    @DefaultValue( text = "false" )
    ValueProperty PROP_ADD_TO_PARENT_POM = new ValueProperty( TYPE, "AddToParentPom" );

    Value<Boolean> getAddToParentPom();
    void setAddToParentPom( String value );
    void setAddToParentPom( Boolean value );

    @Type( base = ILiferayProjectProvider.class )
    ValueProperty PROP_PROJECT_PROVIDER = new ValueProperty( TYPE, "ProjectProvider" ); //$NON-NLS-1$

    Value<ILiferayProjectProvider> getProjectProvider();

    void setProjectProvider( String value );

    void setProjectProvider( ILiferayProjectProvider value );

}
