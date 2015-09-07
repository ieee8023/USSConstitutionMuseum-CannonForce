mkdir -p classes
javac -J-Xms128m -J-Xmx128m -cp `sh getclasspath.sh` -d classes `find src -type f -name "*.java"`
