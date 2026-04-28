#!/usr/bin/env bash
# Staff++ Build Script
# This script compiles Staff++ with proper Java 8 compatibility

set -e

echo "=========================================="
echo "Staff++ v4.0.0 Build Script"
echo "=========================================="
echo ""

# Check if Maven is installed
if ! command -v mvn &> /dev/null; then
    echo "ERROR: Maven is not installed or not in PATH"
    exit 1
fi

echo "Maven version:"
mvn --version
echo ""

# Check Java version
echo "Java version:"
java -version
echo ""

# Clean and package
echo "Building Staff++..."
mvn clean package -DskipTests

echo ""
echo "=========================================="
echo "Build completed successfully!"
echo "=========================================="
echo ""
echo "JAR location: StaffPlusCore/target/Staff+.jar"
echo "Version: 4.0.0"
echo "Target Java: 1.8+"
