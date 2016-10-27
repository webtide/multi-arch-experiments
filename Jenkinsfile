#!groovy

node {
  // System Dependent Locations
  def mvntool = tool name: 'maven3', type: 'hudson.tasks.Maven$MavenInstallation'
  def jdktool = tool name: 'jdk8', type: 'hudson.model.JDK'

  // Environment
  List mvnEnv = ["PATH+MVN=${mvntool}/bin", "PATH+JDK=${jdktool}/bin", "JAVA_HOME=${jdktool}/", "MAVEN_HOME=${mvntool}"]
  mvnEnv.add("MAVEN_OPTS=-Xms256m -Xmx1024m -Djava.awt.headless=true")

  node('linux') {
    stage('Checkout Linux') {
      checkout scm
    }

    stage('Build Linux') {
      withEnv(mvnEnv) {
        timeout(time: 15, unit: 'MINUTES') {
          sh "mvn -B -Dmaven.test.failures.ignore=true clean install"
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
      withEnv(mvnEnv) {
        timeout(time: 15, unit: 'MINUTES') {
          sh "mvn -B -Dmaven.test.failures.ignore=true clean install"
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
      withEnv(mvnEnv) {
        timeout(time: 15, unit: 'MINUTES') {
          sh "mvn -B -Dmaven.test.failures.ignore=true clean install"
          step([$class     : 'JUnitResultArchiver',
                testResults: '**/target/surefire-reports/TEST-*.xml'])
        }
      }
    }
  }
}
// vim: et:ts=2:sw=2:ft=groovy
