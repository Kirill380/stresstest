FROM azul/zulu-openjdk-alpine:11-jre

ENV APP_HOME /opt/app
ENV JAR stresstest-0.0.1-SNAPSHOT.jar
ENV JAVA_OPTS "-server -XX:+UseParallelGC -XX:+PrintCommandLineFlags -showversion"

COPY --chown=guest:users build/libs/${JAR} ${APP_HOME}/lib/${JAR}

USER guest

EXPOSE 8080
WORKDIR ${APP_HOME}
CMD java ${JAVA_OPTS} -jar lib/${JAR}
