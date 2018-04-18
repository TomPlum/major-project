@REM
@REM Copyright (c) 2001-2017 Mathew A. Nelson and Robocode contributors
@REM All rights reserved. This program and the accompanying materials
@REM are made available under the terms of the Eclipse Public License v1.0
@REM which accompanies this distribution, and is available at
@REM http://robocode.sourceforge.net/license/epl-v10.html
@REM

for /l %%x in (1, 1, 600) do (
	java -Xmx1024M -DNOSECURITY=true -XX:+IgnoreUnrecognizedVMOptions --add-opens=java.base/sun.net.www.protocol.jar=ALL-UNNAMED --add-opens=java.base/java.lang.reflect=ALL-UNNAMED --add-opens=java.desktop/sun.awt=ALL-UNNAMED -javaagent:C:\Users\thoma\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\181.4668.1\lib\idea_rt.jar=49888:C:\Users\thoma\AppData\Local\JetBrains\Toolbox\apps\IDEA-U\ch-0\181.4668.1\bin -Dfile.encoding=UTF-8 -classpath "C:\Users\thoma\Dropbox (University)\Year 3\CPU6001 - Major Project (Amanda & Louise)\IntelliJ\MajorProjectv2\robocode_master\robots;C:\Users\thoma\Dropbox (University)\Year 3\CPU6001 - Major Project (Amanda & Louise)\TwitterResources\Twitter4J Master\lib\twitter4j-core-4.0.4.jar;C:\Users\thoma\Dropbox (University)\Year 3\CPU6001 - Major Project (Amanda & Louise)\TwitterResources\emoji-java-4.0.0.jar;C:\Users\thoma\Dropbox (University)\Year 3\CPU6001 - Major Project (Amanda & Louise)\TwitterResources\mongo-java-driver-3.6.1.jar;C:\Users\thoma\Dropbox (University)\Year 3\CPU6001 - Major Project (Amanda & Louise)\IntelliJ\MajorProjectv2\robocode_master\libs\robocode.jar;C:\Users\thoma\Dropbox (University)\Year 3\CPU6001 - Major Project (Amanda & Louise)\TwitterResources\org.json.jar" controller.StartRobocode %*
)


