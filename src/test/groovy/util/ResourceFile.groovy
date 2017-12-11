package util

import org.junit.rules.ExternalResource

class ResourceFile extends ExternalResource {
    private static final String PREFIX = "resourceFile"
    private static final String SUFFIX = ".tmp"
    String res
    File file = null
    InputStream stream

    ResourceFile(String res) {
        this.res = res
    }

    File getFile() throws IOException {
        if (this.file == null) {
            this.createFile()
        }

        return this.file
    }

    InputStream getInputStream() {
        return this.stream
    }

    InputStream createInputStream() {
        return this.getClass().getResourceAsStream(this.res)
    }

    String getContent() throws IOException {
        return this.getContent("utf-8")
    }

    String getContent(String charSet) throws IOException {
        InputStreamReader reader = new InputStreamReader(this.createInputStream(), Charset.forName(charSet))
        char[] tmp = new char[4096]
        StringBuilder b = new StringBuilder()

        try {
            while(true) {
                int len = reader.read(tmp)
                if (len < 0) {
                    reader.close()
                    return b.toString()
                }

                b.append(tmp, 0, len)
            }
        } finally {
            reader.close()
        }
    }

    protected void before() throws Throwable {
        super.before()
        this.stream = this.getClass().getResourceAsStream(this.res)
    }

    protected void after() {
        try {
            this.stream.close()
        } catch (IOException var2) {

        }

        if (this.file != null) {
            this.file.delete()
        }

        super.after()
    }

    private void createFile() throws IOException {
        this.file = File.createTempFile("resourceFile", ".tmp")
        InputStream stream = this.getClass().getResourceAsStream(this.res)

        try {
            FileOutputStream ostream = null

            try {
                ostream = new FileOutputStream(this.file)
                byte[] buffer = new byte[4096]

                while(true) {
                    int len = stream.read(buffer)
                    if (len < 0) {
                        return
                    }

                    ostream.write(buffer, 0, len)
                }
            } finally {
                if (ostream != null) {
                    ostream.close()
                }

            }
        } finally {
            stream.close()
        }
    }
}