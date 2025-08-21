plugins {
    id 'java'
    id 'org.springframework.boot' version '3.5.4' apply false
    id 'io.spring.dependency-management' version '1.1.7' apply false
}

group = 'com.echobeat'
version = '0.0.1-SNAPSHOT'

// Java 버전 설정
allprojects {
    group = 'com.echobeat'
    version = '0.0.1-SNAPSHOT'
    
    repositories {
        mavenCentral()
    }
}

// 모든 서브프로젝트에 공통 설정 적용
subprojects {
    apply plugin: 'java'
    apply plugin: 'org.springframework.boot'
    apply plugin: 'io.spring.dependency-management'
    
    java {
        toolchain {
            languageVersion = JavaLanguageVersion.of(17)
        }
    }
    
    configurations {
        compileOnly {
            extendsFrom annotationProcessor
        }
    }
    
    dependencies {
        // 모든 서브프로젝트 공통 의존성
        compileOnly 'org.projectlombok:lombok'
        annotationProcessor 'org.projectlombok:lombok'
        testImplementation 'org.springframework.boot:spring-boot-starter-test'
        testRuntimeOnly 'org.junit.platform:junit-platform-launcher'
    }
    
    tasks.named('test') {
        useJUnitPlatform()
    }
}
