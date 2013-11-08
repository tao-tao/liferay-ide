package com.liferay.ide.project.ui.action;

import com.liferay.ide.project.core.model.NewLiferayPluginProjectOp;
import com.liferay.ide.project.core.model.NewLiferayProfile;
import com.liferay.ide.project.ui.wizard.NewLiferayPluginProjectWizard;

import org.eclipse.sapphire.ui.Presentation;
import org.eclipse.sapphire.ui.def.DefinitionLoader;
import org.eclipse.sapphire.ui.forms.PropertyEditorActionHandler;
import org.eclipse.sapphire.ui.forms.swt.SapphireDialog;
import org.eclipse.sapphire.ui.forms.swt.SwtPresentation;


public class CreateLiferayProfileActionHandler extends PropertyEditorActionHandler
{

    @Override
    protected Object run( Presentation context )
    {
        if( context instanceof SwtPresentation )
        {
            SwtPresentation swt = (SwtPresentation) context;

            final NewLiferayPluginProjectOp op = op( context );
            final NewLiferayProfile newLiferayProfile = NewLiferayProfile.TYPE.instantiate();

            newLiferayProfile.setProjectProvider( op.getProjectProvider().content() );

            final SapphireDialog dialog =
                new SapphireDialog( swt.shell(), newLiferayProfile, DefinitionLoader.sdef(
                    NewLiferayPluginProjectWizard.class ).dialog( "NewLiferayProfile" ) );

            dialog.setBlockOnOpen( true );
            dialog.open();

            op.setActiveProfiles( newLiferayProfile.getProfileId().content() );
        }

        return null;
    }

    private NewLiferayPluginProjectOp op( Presentation context )
    {
        return context.part().getLocalModelElement().nearest( NewLiferayPluginProjectOp.class );
    }

}
