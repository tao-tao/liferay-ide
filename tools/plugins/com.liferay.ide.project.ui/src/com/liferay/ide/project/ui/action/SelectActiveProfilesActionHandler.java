package com.liferay.ide.project.ui.action;

import com.liferay.ide.project.core.model.NewLiferayPluginProjectOp;
import com.liferay.ide.project.core.model.Profile;
import com.liferay.ide.project.core.model.SelectActiveProfiles;
import com.liferay.ide.project.ui.wizard.NewLiferayPluginProjectWizard;

import org.eclipse.sapphire.ElementList;
import org.eclipse.sapphire.ui.Presentation;
import org.eclipse.sapphire.ui.def.DefinitionLoader;
import org.eclipse.sapphire.ui.forms.PropertyEditorActionHandler;
import org.eclipse.sapphire.ui.forms.swt.SapphireDialog;
import org.eclipse.sapphire.ui.forms.swt.SwtPresentation;


public class SelectActiveProfilesActionHandler extends PropertyEditorActionHandler
{

    @Override
    protected Object run( Presentation context )
    {
        if( context instanceof SwtPresentation )
        {
            final SwtPresentation swt = (SwtPresentation) context;

            final SelectActiveProfiles selectActiveProfiles = SelectActiveProfiles.TYPE.instantiate();

            final SapphireDialog dialog =
                new SapphireDialog( swt.shell(), selectActiveProfiles, DefinitionLoader.sdef(
                    NewLiferayPluginProjectWizard.class ).dialog( "SelectActiveProfiles" ) );


            dialog.setBlockOnOpen( true );
            dialog.open();

            final ElementList<Profile> selectedProfiles = selectActiveProfiles.getSelectedProfiles();

            StringBuilder sb = new StringBuilder();

            for( Profile p : selectedProfiles )
            {
                sb.append( p.getId().content() );
                sb.append( ',' );
            }

            final String string = sb.toString();
            final NewLiferayPluginProjectOp op = op( context );
            op.setActiveProfiles( string.substring( 0, string.length() - 1 ) );
        }

        return null;
    }

    private NewLiferayPluginProjectOp op( Presentation context )
    {
        return context.part().getLocalModelElement().nearest( NewLiferayPluginProjectOp.class );
    }

}
