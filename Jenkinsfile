#!groovy

node {
  // System Dependent Locations

  node('linux') {
    stage('Checkout Linux') {
      checkout scm
    }

    stage('Build Linux') {
      def mvntool = tool name: 'maven3', type: 'hudson.tasks.Maven$MavenInstallation'
      def jdktool = tool name: 'jdk8', type: 'hudson.model.JDK'

      // Environment
      List mvnEnv = ["PATH+MVN=${mvntool}/bin", "PATH+JDK=${jdktool}/bin", "JAVA_HOME=${jdktool}/", "MAVEN_HOME=${mvntool}"]
      mvnEnv.add("MAVEN_OPTS=-Xms256m -Xmx1024m -Djava.awt.headless=true")

      withEnv(mvnEnv) {
        timeout(time: 15, unit: 'MINUTES') {
          sh "mvn -B -Dmaven.test.failure.ignore=true clean install"
          step([$class     : 'JUnitResultArchiver',
                testResults: '**/target/surefire-reports/TEST-*.xml'])
        }
      }
    }
  }

  node('osx') {
    stage('Checkout OSX') {
      checkout scm
    }

    stage('Build OSX') {
      def omvntool = tool name: 'maven3', type: 'hudson.tasks.Maven$MavenInstallation'
      def ojdktool = tool name: 'jdk8', type: 'hudson.model.JDK'

      // Environment
      List omvnEnv = ["PATH+MVN=${omvntool}/bin", "PATH+JDK=${ojdktool}/bin", "JAVA_HOME=${ojdktool}/", "MAVEN_HOME=${omvntool}"]
      omvnEnv.add("MAVEN_OPTS=-Xms256m -Xmx1024m -Djava.awt.headless=true")

      withEnv(omvnEnv) {
        timeout(time: 15, unit: 'MINUTES') {
          sh "mvn -B -Dmaven.test.failure.ignore=true clean install"
          step([$class     : 'JUnitResultArchiver',
                testResults: '**/target/surefire-reports/TEST-*.xml'])
        }
      }
    }
  }

  node('windows') {
    stage('Checkout Windows') {
      checkout scm
    }

    stage('Build Windows') {
      def wmvntool = tool name: 'maven3', type: 'hudson.tasks.Maven$MavenInstallation'
      def wjdktool = tool name: 'jdk8', type: 'hudson.model.JDK'

      // Environment
      List wmvnEnv = ["PATH+MVN=${wmvntool}\\bin", "PATH+JDK=${wjdktool}\\bin", "JAVA_HOME=${wjdktool}/", "MAVEN_HOME=${wmvntool}"]
      wmvnEnv.add("MAVEN_OPTS=-Xms256m -Xmx1024m -Djava.awt.headless=true")

      withEnv(wmvnEnv) {
        timeout(time: 15, unit: 'MINUTES') {
          sh "mvn.cmd -B -Dmaven.test.failure.ignore=true clean install"
          step([$class     : 'JUnitResultArchiver',
                testResults: '**/target/surefire-reports/TEST-*.xml'])
        }
      }
    }
  }
}
// vim: et:ts=2:sw=2:ft=groovy
