package io.fabric8.kubernetes.examples;

import io.fabric8.kubernetes.DefaultKubernetesClient;
import io.fabric8.kubernetes.KubernetesClient;
import io.fabric8.kubernetes.KubernetesClientException;
import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceBuilder;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.PodBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CreateExamples {

  private static final Logger logger = LoggerFactory.getLogger(CreateExamples.class);

  public static void main(String[] args) {
    KubernetesClient client = null;

    String master = "https://localhost:8443/";
    if (args.length == 1) {
      master = args[0];
    }

    try {
      client = new DefaultKubernetesClient.Builder().configFromSysPropsOrEnvVars().masterUrl(master).build();

      Namespace newNamespace = new NamespaceBuilder().withNewMetadata().withName("thisisnew").endMetadata().build();

      System.out.println(
        client.namespaces().create(newNamespace)
      );

      Pod newPod = new PodBuilder().withNewMetadata().withName("newpod").endMetadata()
        .withNewSpec().addNewContainer().withName("newcontainer").withImage("tutum/rabbitmq").endContainer().endSpec().build();

      System.out.println(
        client.pods().inNamespace("thisisnew").create(newPod)
      );

    } catch (KubernetesClientException e) {
      logger.error(e.getMessage(), e);
    } finally {
      if (client != null) {
        client.close();
      }
    }
  }

}
