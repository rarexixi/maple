package org.xi.maple.manager.k8s;

import io.fabric8.kubernetes.api.model.Namespace;
import io.fabric8.kubernetes.api.model.NamespaceList;
import io.fabric8.kubernetes.client.ConfigBuilder;
import io.fabric8.kubernetes.client.KubernetesClient;
import io.fabric8.kubernetes.client.KubernetesClientBuilder;
import org.junit.Test;

public class K8sClientTest {
    @Test
    public  void testK8sClient() {
        ConfigBuilder configBuilder = new ConfigBuilder();
        configBuilder.withMasterUrl("https://192.168.1.12:6443")
                .withClientCertData("LS0tLS1CRUdJTiBDRVJUSUZJQ0FURS0tLS0tCk1JSURJVENDQWdtZ0F3SUJBZ0lJREhxR3hqZ3Ryck13RFFZSktvWklodmNOQVFFTEJRQXdGVEVUTUJFR0ExVUUKQXhNS2EzVmlaWEp1WlhSbGN6QWVGdzB5TkRFeU1ETXhNakkzTkRWYUZ3MHlOVEV5TURNeE1qSTNORFphTURReApGekFWQmdOVkJBb1REbk41YzNSbGJUcHRZWE4wWlhKek1Sa3dGd1lEVlFRREV4QnJkV0psY201bGRHVnpMV0ZrCmJXbHVNSUlCSWpBTkJna3Foa2lHOXcwQkFRRUZBQU9DQVE4QU1JSUJDZ0tDQVFFQXc4L1NNYVFaVGhWOUdrZVYKaFIxeTFsTWc1UGlORnhBWndkdWVsWDYwTU1MdmNjR2djTGpmbFpNS3UwTnJYdnFKclg5OEMyUlVLSWtSUGE1RApaOE9YTEtOTDVkWnJKNjA2dm9DMkc1V3VsQUNKbFhCUmVOZXk5a2tCNG5iYi90bmYvK2FuUTc2Wkt1WU9xLzdsCmlRN2MzNzAvYkNIUUJaMnVyNlBLaUhyUXpQYzdiQjZTai8rdVNpeW9Ec2JTakNBWmZncDJURlRzOEtBd2NTcHQKWU5wYTJNcWtMS3BYZUsrODYwV3FPK29mQWZSd3FOYUpKWUpDQ1YyQUY2TjUzNHo3SVlkOUg4ZjJpaEdqVG8wbApCbzZmUHNIVS9KY1NTdW92dlpHdjZTNit5bDZwaHFBZTQ2UEl5a29KODdTeHI1NXNrZ24vK1IyaytnNHN1T1h6CkVpTnRpd0lEQVFBQm8xWXdWREFPQmdOVkhROEJBZjhFQkFNQ0JhQXdFd1lEVlIwbEJBd3dDZ1lJS3dZQkJRVUgKQXdJd0RBWURWUjBUQVFIL0JBSXdBREFmQmdOVkhTTUVHREFXZ0JTU0MrSkpEMjRFeTRVOWVaUlYyenFQYkZycQp0ekFOQmdrcWhraUc5dzBCQVFzRkFBT0NBUUVBTkNWS080U3B5TmduazRiM0FMbVk2Z1Q0aFB5RnhsY1BsV2FZCkhVQnpNVUR3V3JWbXV3WEY2VU9LTDVCTXp1YXlPYjdYdU5PWnBwR3hMRG1nM3F6eTEwbENRbGJTc3dnU1A0RjUKWmhlMllGNlZxUHZKY0piOEFqRURHV29CTzlNRlVDNXoyOExYb2FpOFpsaVdNV0VTMWRxY1k2Qk13TzVEeWk3dwpHdno3SXlVUjM2QTVZNDdoQmhTVS96dWtVSDdPa3IyMnU2L1RiMjJ6ekJabWlLdU1FWXlOVHV0SjROdU12ZFI0CjdYd29ZZzFXQVFwK3VibkNYOTludnZORWVxNU0xK1JEczFvTEUxUTc4M2I4NkZEVnpVemNqN050THNjbk1kQloKTm9vZmNiTjJlaVNLSzJwdUllRGgzb05sRHBOSnhYblIyVFJHNHNrcXFjNjJTaTZkb3c9PQotLS0tLUVORCBDRVJUSUZJQ0FURS0tLS0tCg==");
        try(KubernetesClient kubernetesClient = new KubernetesClientBuilder()
                .withConfig(configBuilder.build()).build()) {
            NamespaceList list = kubernetesClient.namespaces().list();
            for (Namespace item : list.getItems()) {
                System.out.println(item.getMetadata().getName());
            }
        }

    }
}
