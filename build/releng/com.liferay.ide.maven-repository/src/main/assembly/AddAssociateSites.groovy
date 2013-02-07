def basedir = project.basedir.canonicalPath
def repositoryDir = basedir + "/target/repository"
def contentJar = repositoryDir  + "/content.jar"

def contentDir = basedir  + "/target/content.jar/"

println 'Unzipping content.jar'

def ant = new AntBuilder();   // create an antbuilder

ant.unzip( src: contentJar, dest:contentDir,  overwrite:"true" )

println 'Modify content.xml to add associate sites for Liferay IDE'

File contentXml =  new File( contentDir, "content.xml" )
def parser = new XmlParser()
def root = parser.parseText( contentXml.text )

addAssociateSite( root, "http://download.eclipse.org/technology/m2e/milestones/1.3/" )
addAssociateSite( root, "http://download.eclipse.org/m2e-wtp/milestones/juno/0.17.0/" )

root.references

println 'Overwriting content.xml'
def writer = new StringWriter()
def printer = new XmlNodePrinter( new PrintWriter( writer ) )
printer.setPreserveWhitespace( true )
printer.print( root )
def result = writer.toString()

contentXml.text = result

println 'Zipping back customized content.jar'
ant.zip( destFile: contentJar, baseDir:contentDir )

def addAssociateSite( root, siteUrl )
{
    def refs = root.references

    if( !refs || refs.size() == 0 )
    {
        def newRefs = new Node( root, 'references' )
        newRefs.@size = "4"
        root.children().add( 1, newRefs )
        refs = root.references
    }

    new Node( refs.get( 0 ), 'repository', [ uri:siteUrl, url:siteUrl, type:'1', options:'1'] )
    new Node( refs.get( 0 ), 'repository', [ uri:siteUrl, url:siteUrl, type:'0', options:'1'] )
}

/*
def zipSite = basedir  + "/target/${project.artifactId}-${project.version}.zip"

println 'Deleting old zip site'
new File( zipSite ).delete()

def unqualifiedVersion = project.properties.getProperty("unqualifiedVersion")
def buildQualifier = project.properties.getProperty("buildQualifier")

println 'Zipping updated site'
ant.zip( destFile: zipSite, baseDir:repositoryDir )

println 'Copying to final version'
File newZipSite = new File( basedir + "/target/Liferay_IDE_${unqualifiedVersion}.${buildQualifier}-updatesite.zip" )
ant.copy( file:zipSite, tofile:newZipSite )
*/