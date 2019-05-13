
File magicFile = new File( basedir, '/target/magic-file.md' )
magicFile.delete()
assert !magicFile.exists()
