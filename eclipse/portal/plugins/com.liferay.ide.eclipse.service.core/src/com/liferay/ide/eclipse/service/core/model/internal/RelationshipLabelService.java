package com.liferay.ide.eclipse.service.core.model.internal;

import com.liferay.ide.eclipse.service.core.model.IRelationship;
import com.liferay.ide.eclipse.service.core.model.IServiceBuilder;

import org.eclipse.sapphire.modeling.IModelElement;
import org.eclipse.sapphire.services.DerivedValueService;
import org.eclipse.sapphire.services.DerivedValueServiceData;


public class RelationshipLabelService extends DerivedValueService {

	@Override
	protected void initDerivedValueService()
	{
	}

	@Override
	protected DerivedValueServiceData compute() {
		String value = null;
		IServiceBuilder sb = context( IModelElement.class ).nearest( IServiceBuilder.class );

		if ( sb != null )
		{
			if ( sb.getShowRelationshipLabels().getContent() )
			{
				value = ( context( IRelationship.class ) ).getForeignKeyColumnName().getContent();
			}
		}

		if ( value == null )
		{
			value = "";
		}

		return new DerivedValueServiceData( value );
	}

}
