FROM adoptopenjdk/openjdk8:jdk8u212-b04 as jdk

FROM amazonlinux:2.0.20200207.1

ENV JAVA_HOME=/opt/java/openjdk \
    PATH="/opt/java/openjdk/bin:$PATH"
COPY --from=jdk /opt/java/openjdk /opt/java/openjdk

CMD ["/bin/bash"]
