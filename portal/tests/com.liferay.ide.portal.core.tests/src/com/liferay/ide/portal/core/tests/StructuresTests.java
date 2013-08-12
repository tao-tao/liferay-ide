
package com.liferay.ide.portal.core.tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import com.liferay.ide.portal.core.structures.model.DynamicElement;
import com.liferay.ide.portal.core.structures.model.DynamicElementMetadata;
import com.liferay.ide.portal.core.structures.model.Entry;
import com.liferay.ide.portal.core.structures.model.Root;
import com.liferay.ide.portal.core.structures.model.Structure;

import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.sapphire.Element;
import org.eclipse.sapphire.ElementList;
import org.junit.After;
import org.junit.Test;

/**
 * @author Gregory Amerson
 */
public class StructuresTests extends PortalCoreTests
{

    static final IPath DDM_STRUCTURE_BASIC_DOCUMENT = new Path( "structures/ddm_structure_basic_document.xml" );
    static final IPath DDM_STRUCTURE_DDL = new Path( "structures/ddm_structure_ddl.xml" );
    static final IPath DDM_STRUCTURE = new Path( "structures/ddmstructure.xml" );
    static final IPath DOCUMENT_LIBRARY_STRUCTURES = new Path( "structures/document-library-structures.xml" );
    static final IPath DYNAMIC_DATA_MAPPING_STRUCTURES_WRONG_FORMAT = new Path(
                    "structures/dynamic-data-mapping-structures-wrong-format.xml" );
    static final IPath DYNAMIC_DATA_MAPPING_STRUCTURES = new Path(
        "structures/dynamic-data-mapping-structures.xml" );
    static final IPath TEST_DDM_STRUCTURE_ALL_FIELDS =
        new Path( "structures/test-ddm-structure-all-fields.xml" );
    static final IPath TEST_JOURNAL_CONTENT_BOOLEAN_REPEATABLE_FIELD = new Path(
        "structures/test-journal-content-boolean-repeatable-field.xml" );
    static final IPath TEST_JOURNAL_CONTENT_DOC_LIBRARY_FIELD = new Path(
        "structures/test-journal-content-doc-library-field.xml" );
    static final IPath TEST_JOURNAL_CONTENT_LINK_TO_PAGE_FIELD = new Path(
        "structures/test-journal-content-link-to-page-field.xml" );
    static final IPath TEST_JOURNAL_CONTENT_LIST_FIELD = new Path( "structures/test-journal-content-list-field.xml" );
    static final IPath TEST_JOURNAL_CONTENT_MULTI_LIST_FIELD = new Path(
        "structures/test-journal-content-multi-list-field.xml" );
    static final IPath TEST_JOURNAL_CONTENT_NESTED_FIELDS = new Path(
        "structures/test-journal-content-nested-fields.xml" );
    static final IPath TEST_JOURNAL_CONTENT_TEXT_AREA_FIELD = new Path(
        "structures/test-journal-content-text-area-field.xml" );
    static final IPath TEST_JOURNAL_CONTENT_TEXT_BOX_REPEATABLE_FIELD = new Path(
        "structures/test-journal-content-text-box-repeatable-field.xml" );
    static final IPath TEST_JOURNAL_CONTENT_TEXT_FIELD = new Path( "structures/test-journal-content-text-field.xml" );

    private Element currentElement;

    @After
    public void cleanup() throws Exception
    {
        if( this.currentElement != null )
        {
            if( ! this.currentElement.disposed() )
            {
                this.currentElement.dispose();
            }

            this.currentElement = null;
        }

        super.cleanup();
    }

    @Test
    public void testDDMStructureBasicDocumentRead() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), DDM_STRUCTURE_BASIC_DOCUMENT, Root.TYPE );

        setElement( element );

        final Root root = element.nearest( Root.class );

        assertNotNull( root );

        assertEquals( "en_US", root.getAvailableLocales().content() );

        assertEquals( "en_US", root.getDefaultLocale().content() );

        final ElementList<DynamicElement> dynamicElements = root.getDynamicElements();

        assertEquals( 154, dynamicElements.size() );

        final DynamicElement dynamicElement = dynamicElements.get( 1 );

        assertNotNull( dynamicElement );

        assertEquals( "string", dynamicElement.getDataType().content( false ) );

        assertEquals( "ClimateForcast_COMMAND_LINE", dynamicElement.getName().content( false ) );

        assertEquals( "text", dynamicElement.getType().content( false ) );

        final DynamicElementMetadata metaData = dynamicElement.getMetadata().content( false );

        assertNotNull( metaData );

        assertEquals( "en_US", metaData.getLocale().content( false ) );

        final ElementList<Entry> entries = metaData.getEntries();

        assertNotNull( entries );

        assertEquals( 4, entries.size() );

        final Entry entry1 = entries.get( 0 );

        assertNotNull( entry1 );

        assertEquals( "label", entry1.getName().content( false ) );

        assertEquals( "metadata.ClimateForcast.COMMAND_LINE", entry1.getValue().content( false ) );

        final Entry entry2 = entries.get( 2 );

        assertNotNull( entry2 );

        assertEquals( "required", entry2.getName().content( false ) );

        assertEquals( "false", entry2.getValue().content( false ) );
    }

    private void setElement( Element element )
    {
        assertNotNull( element );

        this.currentElement = element;
    }

    @Test
    public void testDocumentLibraryStructuresRead() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), DOCUMENT_LIBRARY_STRUCTURES, Root.TYPE );

        setElement( element );

        final Root root = element.nearest( Root.class );

        assertNotNull( root );

        final ElementList<Structure> structures = root.getStructures();

        assertNotNull( structures );
        assertEquals( 8, structures.size() );

        final Structure structure = structures.get( 2 );

        assertNotNull( structure );
        assertEquals( "Learning Module Metadata", structure.getName().content( false ) );
        assertEquals( "Learning Module Metadata", structure.getDescription().content( false ) );

        final Root structureRoot = structure.getRoot().content( false );

        assertNotNull( structureRoot );
        assertEquals( "[$LOCALE_DEFAULT$]", structureRoot.getAvailableLocales().content( false ) );
        assertEquals( "[$LOCALE_DEFAULT$]", structureRoot.getDefaultLocale().content( false ) );

        final ElementList<DynamicElement> dynamicElements = structureRoot.getDynamicElements();

        assertNotNull( dynamicElements );
        assertEquals( 4, dynamicElements.size() );

        final DynamicElement dynamicElement = dynamicElements.get( 1 );

        assertNotNull( dynamicElement );
        assertEquals( "string", dynamicElement.getDataType().content( false ) );
        assertEquals( "keyword", dynamicElement.getIndexType().content( false ) );
        assertEquals( true, dynamicElement.isMultiple().content( false ) );
        assertEquals( "select3212", dynamicElement.getName().content( false ) );
        assertEquals( false, dynamicElement.isReadOnly().content( false ) );
        assertEquals( false, dynamicElement.isRequired().content( false ) );
        assertEquals( true, dynamicElement.isShowLabel().content( false ) );
        assertEquals( "select", dynamicElement.getType().content( false ) );

        final DynamicElementMetadata metadata = dynamicElement.getMetadata().content( false );

        assertNotNull( metadata );
        assertEquals( "[$LOCALE_DEFAULT$]", metadata.getLocale().content( false ) );

        final ElementList<Entry> entries = metadata.getEntries();

        assertNotNull( entries );
        assertEquals( 3, entries.size() );

        final ElementList<DynamicElement> childDynamicElements = dynamicElement.getDynamicElements();

        assertNotNull( childDynamicElements );
        assertEquals( 3, childDynamicElements.size() );

        final DynamicElement childDynamicElement = childDynamicElements.get( 1 );

        assertNotNull( childDynamicElement );
        assertEquals( "2_0", childDynamicElement.getName().content( false ) );
        assertEquals( "option", childDynamicElement.getType().content( false ) );
        assertEquals( "2", childDynamicElement.getValue().content( false ) );

        final DynamicElementMetadata childMetadata = childDynamicElement.getMetadata().content( false );

        assertNotNull( childMetadata );
        assertEquals( "[$LOCALE_DEFAULT$]", childMetadata.getLocale().content( false ) );

        final ElementList<Entry> childEntries = childMetadata.getEntries();

        assertNotNull( childEntries );
        assertEquals( 1, childEntries.size() );

        final Entry childEntry = childEntries.get( 0 );

        assertNotNull( childEntry );
        assertEquals( "label", childEntry.getName().content( false ) );
        assertEquals( "2.0", childEntry.getValue().content( false ) );
    }

    @Test
    public void testDDMStructureDDLRead() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), DDM_STRUCTURE_DDL, Root.TYPE );

        setElement( element );

        final Root root = element.nearest( Root.class );

        assertNotNull( root );
        assertEquals( "en_US", root.getAvailableLocales().content( false ) );
        assertEquals( "en_US", root.getDefaultLocale().content( false ) );

        final ElementList<DynamicElement> dynamicElements = root.getDynamicElements();

        assertNotNull( dynamicElements );
        assertEquals( 1, dynamicElements.size() );

        final DynamicElement dynamicElement = dynamicElements.get( 0 );

        assertNotNull( dynamicElement );
        assertEquals( "string", dynamicElement.getDataType().content( false ) );
        assertEquals( "text2102", dynamicElement.getName().content( false ) );
        assertEquals( "text", dynamicElement.getType().content( false ) );

        final DynamicElementMetadata metadata = dynamicElement.getMetadata().content( false );

        assertNotNull( metadata );
        assertEquals( "en_US", metadata.getLocale().content( false ) );

        final ElementList<Entry> entries = metadata.getEntries();

        assertNotNull( entries );
        assertEquals( 7, entries.size() );

        final Entry entry1 = entries.get( 1 );

        assertNotNull( entry1 );
        assertEquals( "showLabel", entry1.getName().content( false ) );
        assertEquals( "true", entry1.getValue().content( false ) );

        final Entry entry2 = entries.get( 3 );

        assertNotNull( entry2 );
        assertEquals( "predefinedValue", entry2.getName().content( false ) );
        assertEquals( "", entry2.getValue().content( false ) );
    }

    @Test
    public void testDDMStructureRead() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), DDM_STRUCTURE, Root.TYPE );

        setElement( element );

        final Root root = element.nearest( Root.class );

        assertNotNull( root );
        assertEquals( "en_US,hu_HU", root.getAvailableLocales().content( false ) );
        assertEquals( "en_US", root.getDefaultLocale().content( false ) );

        final ElementList<DynamicElement> dynamicElements = root.getDynamicElements();

        assertNotNull( dynamicElements );
        assertEquals( 1, dynamicElements.size() );

        final DynamicElement dynamicElement = dynamicElements.get( 0 );

        assertNotNull( dynamicElement );
        assertEquals( "true", dynamicElement.getAutoGeneratedName().content( false ) );
        assertEquals( "date", dynamicElement.getDataType().content( false ) );
        assertEquals( "ddm", dynamicElement.getFieldNamespace().content( false ) );
        assertEquals( "", dynamicElement.getIndexType().content( false ) );
        assertEquals( "date_an", dynamicElement.getName().content( false ) );
        assertEquals( "ddm_date", dynamicElement.getType().content( false ) );

        final ElementList<DynamicElementMetadata> metadatas = dynamicElement.getMetadatas();

        assertNotNull( metadatas );
        assertEquals( 2, metadatas.size() );

        final DynamicElementMetadata metadata = metadatas.get( 1 );

        assertNotNull( metadata );
        assertEquals( "hu_HU", metadata.getLocale().content( false ) );

        final ElementList<Entry> entries = metadata.getEntries();

        assertNotNull( entries );
        assertEquals( 7, entries.size() );

        final Entry entry1 = entries.get( 0 );

        assertNotNull( entry1 );
        assertEquals( "label", entry1.getName().content( false ) );
        assertEquals( "Date_HU", entry1.getValue().content( false ) );

        final Entry entry2 = entries.get( 6 );

        assertNotNull( entry2 );
        assertEquals( "fieldCssClass", entry2.getName().content( false ) );
        assertEquals( "w25", entry2.getValue().content( false ) );
    }

    @Test
    public void testDynamicDataMappingStructuresWrongFormatRead() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), DYNAMIC_DATA_MAPPING_STRUCTURES_WRONG_FORMAT, Root.TYPE );

        setElement( element );

        final Root root = element.nearest( Root.class );

        assertNotNull( root );
        assertEquals( "Contacts", root.getName().content( false ) );
        assertEquals( "Contacts", root.getDescription().content( false ) );

        final Root childRoot = root.getRoot().content( false );

        assertNotNull( childRoot );
        assertEquals( "en_US", childRoot.getAvailableLocales().content( false ) );
        assertEquals( "en_US", childRoot.getDefaultLocale().content( false ) );

        final ElementList<DynamicElement> dynamicElements = childRoot.getDynamicElements();

        assertNotNull( dynamicElements );
        assertEquals( 4, dynamicElements.size() );

        final DynamicElement dynamicElement = dynamicElements.get( 1 );

        assertNotNull( dynamicElement );
        assertEquals( "string", dynamicElement.getDataType().content( false ) );
        assertEquals( "true", dynamicElement.getMultiple().content( false ) );
        assertEquals( "select3212", dynamicElement.getName().content( false ) );
        assertEquals( "false", dynamicElement.getReadOnly().content( false ) );
        assertEquals( "true", dynamicElement.getShowLabel().content( false ) );
        assertEquals( "select", dynamicElement.getType().content( false ) );

        final DynamicElementMetadata metadata = dynamicElement.getMetadata().content( false );

        assertNotNull( metadata );
        assertEquals( "en_US", metadata.getLocale().content( false ) );

        final ElementList<Entry> entries = metadata.getEntries();

        assertNotNull( entries );
        assertEquals( 3, entries.size() );

        final Entry entry1 = entries.get( 0 );

        assertNotNull( entry1 );
        assertEquals( "label", entry1.getName().content( false ) );
        assertEquals( "Version", entry1.getValue().content( false ) );

        final Entry entry2 = entries.get( 1 );

        assertNotNull( entry2 );
        assertEquals( "predefinedValue", entry2.getName().content( false ) );
        assertEquals( "", entry2.getValue().content( false ) );

        final ElementList<DynamicElement> childDynamicElements = dynamicElement.getDynamicElements();

        assertNotNull( childDynamicElements );
        assertEquals( 3, childDynamicElements.size() );

        final DynamicElement childDynamicElement = childDynamicElements.get( 1 );

        assertNotNull( childDynamicElement );
        assertEquals( "2_0", childDynamicElement.getName().content( false ) );
        assertEquals( "option", childDynamicElement.getType().content( false ) );
        assertEquals( "2", childDynamicElement.getValue().content( false ) );

        final DynamicElementMetadata childMetadata = childDynamicElement.getMetadata().content( false );

        assertNotNull( childMetadata );
        assertEquals( "en_US", childMetadata.getLocale().content( false ) );

        final ElementList<Entry> childEntries = childMetadata.getEntries();

        assertNotNull( childEntries );
        assertEquals( 1, childEntries.size() );

        final Entry childEntry = childEntries.get( 0 );

        assertNotNull( childEntry );
        assertEquals( "label", childEntry.getName().content( false ) );
        assertEquals( "2.0", childEntry.getValue().content( false ) );
    }

    public void testDynamicDataMappingStructuresRead() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), DYNAMIC_DATA_MAPPING_STRUCTURES, Root.TYPE );

        setElement( element );

        final Root root = element.nearest( Root.class );

        assertNotNull( root );

        final ElementList<Structure> structures = root.getStructures();

        assertNotNull( structures );
        assertEquals( 6, structures.size() );

        final Structure structure = structures.get( 4 );

        assertNotNull( structure );
        assertEquals( "Meeting Minutes", structure.getName().content( false ) );
        assertEquals( "Meeting Minutes", structure.getDescription().content( false ) );

        final Root structureRoot = structure.getRoot().content( false );

        assertNotNull( structureRoot );
        assertEquals( "en_US", structureRoot.getAvailableLocales().content( false ) );
        assertEquals( "en_US", structureRoot.getDefaultLocale().content( false ) );

        final ElementList<DynamicElement> dynamicElements = structureRoot.getDynamicElements();

        assertNotNull( dynamicElements );
        assertEquals( 7, dynamicElements.size() );

        final DynamicElement dynamicElement1 = dynamicElements.get( 0 );

        assertNotNull( dynamicElement1 );
        assertEquals( "file-upload", dynamicElement1.getDataType().content( false ) );
        assertEquals( "ddm", dynamicElement1.getFieldNamespace().content( false ) );
        assertEquals( "attachment", dynamicElement1.getName().content( false ) );
        assertEquals( "false", dynamicElement1.getRequired().content( false ) );
        assertEquals( "true", dynamicElement1.getShowLabel().content( false ) );
        assertEquals( "ddm-fileupload", dynamicElement1.getType().content( false ) );

        final DynamicElementMetadata metadata1 = dynamicElement1.getMetadata().content( false );

        assertNotNull( metadata1 );
        assertEquals( "en_US", metadata1.getLocale().content( false ) );

        final ElementList<Entry> firstEntries = metadata1.getEntries();

        assertNotNull( firstEntries );
        assertEquals( 5, firstEntries.size() );

        final Entry firstEntry1 = firstEntries.get( 0 );

        assertNotNull( firstEntry1 );
        assertEquals( "acceptFiles", firstEntry1.getName().content( false ) );
        assertEquals( "*", firstEntry1.getValue().content( false ) );

        final Entry entry2 = firstEntries.get( 1 );

        assertNotNull( entry2 );
        assertEquals( "folder", entry2.getName().content( false ) );
        assertEquals( "{\"folderId\":0,\"folderName\":\"Documents Home\"}", entry2.getValue().content( false ) );

        final DynamicElement dynamicElement2 = dynamicElements.get( 2 );

        assertNotNull( dynamicElement2 );
        assertEquals( "string", dynamicElement2.getDataType().content( false ) );
        assertEquals( "description", dynamicElement2.getName().content( false ) );
        assertEquals( "false", dynamicElement2.getRequired().content( false ) );
        assertEquals( "true", dynamicElement2.getShowLabel().content( false ) );
        assertEquals( "textarea", dynamicElement2.getType().content( false ) );
        assertEquals( "100", dynamicElement2.getWidth().content( false ) );

        final DynamicElementMetadata metadata2 = dynamicElement2.getMetadata().content( false );

        assertNotNull( metadata2 );
        assertEquals( "en_US", metadata2.getLocale().content( false ) );

        final ElementList<Entry> secondEntries = metadata2.getEntries();

        assertNotNull( secondEntries );
        assertEquals( 2, secondEntries.size() );

        final Entry secondEntry1 = secondEntries.get( 0 );

        assertNotNull( secondEntry1 );
        assertEquals( "label", secondEntry1.getValue().content( false ) );
        assertEquals( "Description", secondEntry1.getValue().content( false ) );

        final Entry secondEntry2 = secondEntries.get( 1 );

        assertNotNull( secondEntry2 );
        assertEquals( "predefinedValue", secondEntry2.getName().content( false ) );
        assertEquals( "", secondEntry2.getValue().content( false ) );
    }

    public void testDDMStructureAllFields() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), TEST_DDM_STRUCTURE_ALL_FIELDS, Root.TYPE );

        setElement( element );

        final Root root = element.nearest( Root.class );

        assertNotNull( root );
        assertEquals( "en_US", root.getAvailableLocales().content( false ) );
        assertEquals( "en_US", root.getDefaultLocale().content( false ) );

        final ElementList<DynamicElement> dynamicElements = root.getDynamicElements();

        assertNotNull( dynamicElements );
        assertEquals( 12, dynamicElements.size() );

        final DynamicElement dynamicElement = dynamicElements.get( 6 );

        assertNotNull( dynamicElement );
        assertEquals( "string", dynamicElement.getDataType().content( false ) );
        assertEquals( "text", dynamicElement.getIndexType().content( false ) );
        assertEquals( "contact", dynamicElement.getName().content( false ) );
        assertEquals( "true", dynamicElement.getRepeatable().content( false ) );
        assertEquals( "false", dynamicElement.getRequired().content( false ) );
        assertEquals( "true", dynamicElement.getShowLabel().content( false ) );
        assertEquals( "text", dynamicElement.getType().content( false ) );
        assertEquals( "25", dynamicElement.getWidth().content( false ) );

        final DynamicElementMetadata metadata = dynamicElement.getMetadata().content( false );

        assertNotNull( metadata );
        assertEquals( "en_US", metadata.getLocale().content( false ) );

        final ElementList<Entry> entries = metadata.getEntries();

        assertNotNull( entries );
        assertEquals( 3, entries.size() );

        final Entry entry = entries.get( 0 );

        assertNotNull( entry );
        assertEquals( "label", entry.getName().content( false ) );
        assertEquals( "Contact", entry.getValue().content( false ) );

        final ElementList<DynamicElement> childDynamicElements = dynamicElement.getDynamicElements();

        assertNotNull( childDynamicElements );
        assertEquals( 1, childDynamicElements.size() );

        final DynamicElement childDynamicElement = childDynamicElements.get( 0 );

        assertNotNull( childDynamicElement );
        assertEquals( "string", childDynamicElement.getDataType().content( false ) );
        assertEquals( "text", childDynamicElement.getIndexType().content( false ) );
        assertEquals( "contact", childDynamicElement.getName().content( false ) );
        assertEquals( "true", childDynamicElement.getRepeatable().content( false ) );
        assertEquals( "false", childDynamicElement.getRequired().content( false ) );
        assertEquals( "true", childDynamicElement.getShowLabel().content( false ) );
        assertEquals( "text", childDynamicElement.getType().content( false ) );
        assertEquals( "25", childDynamicElement.getWidth().content( false ) );

        final DynamicElementMetadata childMetaData = childDynamicElement.getMetadata().content( false );

        assertNotNull( childMetaData );
        assertEquals( "en_US", childMetaData.getLocale().content( false ) );

        final ElementList<Entry> childEntries = childMetaData.getEntries();

        assertNotNull( childEntries );
        assertEquals( 3, childEntries.size() );

        final Entry childEntry = childEntries.get( 1 );

        assertNotNull( childEntry );
        assertEquals( "predefinedValue", childEntry.getName().content( false ) );
        assertEquals( "", childEntry.getValue().content( false ) );

        final ElementList<DynamicElement> childChildDynamicElements = childDynamicElement.getDynamicElements();

        assertNotNull( childChildDynamicElements );
        assertEquals( 1, childChildDynamicElements.size() );

        final DynamicElement childChildDynamicElement = childChildDynamicElements.get( 0 );

        assertNotNull( childChildDynamicElement );
        assertEquals( "string", childChildDynamicElement.getDataType().content( false ) );
        assertEquals( "text", childChildDynamicElement.getIndexType().content( false ) );
        assertEquals( "contact", childChildDynamicElement.getName().content( false ) );
        assertEquals( "true", childChildDynamicElement.getRepeatable().content( false ) );
        assertEquals( "false", childChildDynamicElement.getRequired().content( false ) );
        assertEquals( "true", childChildDynamicElement.getShowLabel().content( false ) );
        assertEquals( "text", childChildDynamicElement.getType().content( false ) );
        assertEquals( "25", childChildDynamicElement.getWidth().content( false ) );

        final DynamicElementMetadata childChildMetadata = childChildDynamicElement.getMetadata().content( false );

        assertNotNull( childChildMetadata );
        assertEquals( "en_US", childChildMetadata.getLocale().content( false ) );

        final ElementList<Entry> childChildEntries = childChildMetadata.getEntries();

        assertNotNull( childChildEntries );
        assertEquals( 3, childChildEntries.size() );

        final Entry childChildEntry = childChildEntries.get( 2 );

        assertNotNull( childChildEntry );
        assertEquals( "tip", childChildEntry.getName().content( false ) );
        assertEquals( "", childChildEntry.getValue().content( false ) );
    }

    public void testJournalContentBooleanRepeatableFieldRead() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), TEST_JOURNAL_CONTENT_BOOLEAN_REPEATABLE_FIELD, Root.TYPE );
        
        setElement( element );
        
        Root root = element.nearest( Root.class );
        
        assertNotNull( root );
        assertEquals( "en_US", root.getAvailableLocales().content( false ) );
        assertEquals( "en_US", root.getDefaultLocale().content( false ) );
        
        ElementList<DynamicElement> dynamicElements = root.getDynamicElements();
        
        assertNotNull( dynamicElements );
        assertEquals( 2, dynamicElements.size() );
        
        DynamicElement dynamicElement =dynamicElements.get( 1 );
        
        assertNotNull( dynamicElement );
        assertEquals( "1SYNQuhg", dynamicElement.getInstanceID().content( false ) );
        assertEquals( "boolean", dynamicElement.getName().content( false ) );
        assertEquals( "boolean", dynamicElement.getType().content( false ) );
        assertEquals( "keyword", dynamicElement.getIndexType().content( false ) );
        
        
    }
    
    public void testJournalContentDocLibraryFieldRead() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), TEST_JOURNAL_CONTENT_DOC_LIBRARY_FIELD, Root.TYPE );
        
        
    }
    
    public void testJournalContentLinkToPageFieldRead() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), TEST_JOURNAL_CONTENT_LINK_TO_PAGE_FIELD, Root.TYPE );
        
    }
    
    public void testJournalContentListField() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), TEST_JOURNAL_CONTENT_LIST_FIELD, Root.TYPE );
        
    }
    
    public void testJournalContentMultiListField() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), TEST_JOURNAL_CONTENT_MULTI_LIST_FIELD, Root.TYPE );
    }
    
    public void testJournalContentNestedFields() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), TEST_JOURNAL_CONTENT_NESTED_FIELDS, Root.TYPE );
    }
    
    public void testJournalContentTextAreaField() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), TEST_JOURNAL_CONTENT_TEXT_AREA_FIELD, Root.TYPE );
    }
    
    public void testJournalContentTextBoxRepeatableField() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), TEST_JOURNAL_CONTENT_TEXT_BOX_REPEATABLE_FIELD, Root.TYPE );
    }
    
    public void testJournalContentTextField() throws Exception
    {
        final Element element = getElementFromFile( getCurrentProject(), TEST_JOURNAL_CONTENT_TEXT_FIELD, Root.TYPE );
    }
}
