
Tout d'abord, il faut définir le langague de l'application. Dans mon cas, c'est Java.
```
language: java
```

Après on peut choisir dans quelle version on veut tester notre programme, ça ce fait en parallèle. On peut après, mettre des scripts et/ou options différents pour chaque version.  
```
jdk:
  - oraclejdk8
  - oraclejdk7
  - openjdk7
```

Avant l'instalation, une mise à jour est faite pour les paquets pour éviter les problèmes 
```
before_install:
  - sudo apt-get update
```

Conservation du référentiel local maven entre les différents exécutions afin d'améliorez la vitesse de construction pour les prochaines.
```
cache:
  directories:
  - $HOME/.m2
```
 
Définition des différents stages de pipeline
```
stages: 
  - build
  - test
  - deploy
```

Définition des scripts pour chaque stage
```  
jobs:
  include:
    - stage: build
      script:
        - mvn compile
        - mvn test
        - mvn package
    - stage: test
      script:
        - mvn verify
    - stage: deploy  
```

L'intégration continue se fait seulement sur les commits de la branche principale master.
```
branches:
  only:
    - master
```

Après que la réussite du pipline le message 'Build stages completed' s'affiche.
```
after_success:
  - echo "Build stages completed"
```

Dans le cas d'échec, un mail est enoyé à moi pour signaler l'échec du building
```
notifications:
  email:
    recipients:
      - aiman.rkyek1@gmail.com
    on_success: never 
    on_failure: always 
```
