
File magicFile1 = new File( basedir, '/target/magic-file-1.md' )
magicFile1.delete()
assert !magicFile1.exists()

File magicFile2 = new File( basedir, '/target/magic-file-2.md' )
magicFile2.delete()
assert !magicFile2.exists()
