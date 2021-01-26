#!/bin/bash -e
if [ -f ~/ForgeryTools.jar ]; then
	canforgery=1
else
	echo "Forgery tools not found. As of the time of writing, the Forgery tooling is not public."
	echo "Performing a Fabric build only."
	echo
	canforgery=0
fi
rm -rf build/libs
echo Building for Fabric...
gw clean build
rm build/libs/*-dev.jar
raw=$(echo build/libs/skychunk*.jar)
fabric=$(echo "$raw" |sed 's/skychunk-/skychunk-fabric-/')
mv "$raw" "$fabric"
if [ "$canforgery" == "1" ]; then
	cd forgery
	echo Building Forgery runtime...
	gw clean build
	cd ..
	forge=$(echo "$fabric" | sed "s/fabric/forge/")
	echo Running Forgery...
	java -jar ~/ForgeryTools.jar "$fabric" "$forge" ~/.gradle/caches/fabric-loom/mappings/intermediary-1.16.5-v2.tiny ~/.gradle/caches/forge_gradle/minecraft_repo/versions/1.16.5/mcp_mappings.tsrg ./forgery/build/libs/forgery.jar ~/.gradle/caches/fabric-loom/minecraft-1.16.5-intermediary-net.fabricmc.yarn-1.16.5+build.3-v2.jar space.bbkr.skychunk
fi
echo Done
