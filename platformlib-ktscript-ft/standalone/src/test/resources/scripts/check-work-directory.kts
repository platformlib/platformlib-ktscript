import java.nio.file.Paths

println("Current directory is " + Paths.get(".").normalize().toAbsolutePath())