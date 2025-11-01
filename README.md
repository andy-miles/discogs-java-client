<a name="readme-top"></a>
<!-- Template Credit: Othneil Drew (https://github.com/othneildrew),
                      https://github.com/othneildrew/Best-README-Template/tree/master -->
<!-- PROJECT SHIELDS -->
<div align="center">

[![Contributors][contributors-shield]][contributors-url]
[![Forks][forks-shield]][forks-url]
[![Stargazers][stars-shield]][stars-url]
[![Issues][issues-shield]][issues-url]
[![LinkedIn][linkedin-shield]][linkedin-url]

</div>

<!-- PROJECT LOGO -->
<br />
<div align="center">
  <a href="https://www.discogs.com/">
    <h1>Discogs</h1>
  </a>
  <h3 align="center">discogs-java-client</h3>

  <p align="center">
    A Java client to access the Discogs API
    <br />
    <a href="https://www.amilesend.com/discogs-java-client"><strong>Maven Project Info</strong></a>
    -
    <a href="https://www.amilesend.com/discogs-java-client/apidocs/index.html"><strong>Javadoc</strong></a>
    <br />
    <a href="https://github.com/andy-miles/discogs-java-client/issues">Report Bug</a>
    -
    <a href="https://github.com/andy-miles/discogs-java-client/issues">Request Feature</a>
  </p>
</div>


<!-- TABLE OF CONTENTS -->
<details>
  <summary>Table of Contents</summary>
  <ol>
    <li>
      <a href="#usage">Usage</a>
      <ul>
        <li><a href="#getting-started">Getting Started</a></li>
        <li><a href="#recipes">Recipes</a></li>
      </ul>
    </li>
    <li><a href="#contributing">Contributing</a></li>
    <li><a href="#license">License</a></li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

<!-- ABOUT THE PROJECT -->
# About The Project

A client for Java programmatic access to [The Discogs API](https://www.discogs.com/developers).

<div align="right">(<a href="#readme-top">back to top</a>)</div>

<a name="usage"></a>
# Usage
<a name="getting-started"></a>
## Getting Started

1. If you require access to authenticated API calls, or access to a user's resources, you'll need to obtain 
   the required authorization per the [API documentation](https://www.discogs.com/developers#page:authentication,header:authentication-discogs-auth-flow).
2. Include this package as a dependency in your project. Note: This package is published to both
   GitHub and Maven Central repositories.

   ```xml
   <dependency>
       <groupId>com.amilesend</groupId>
       <artifactId>discogs-java-client</artifactId>
       <version>1.0.4</version>
   </dependency>
   ```
3. Instantiate the client:
   1. Unauthenticated client with limited API access, no images, and stricter rate-limits:
      ```java
      Discogs client = Discogs.newUnauthenticatedInstance("MyUserAgent/1.0");
      
      // Access APIs
      DatabaseApi databaseApi = client.getDatabaseApi();
      GetArtistInformationRequest request = GetArtistInformationRequest.builder()
              .artistId(260935L)
              .build();
      GetArtistInformationResponse response = databaseApi.getArtistInformation(request);
      ```
   2. Consumer/application key + secret with limited API access:
      ```java
      Discogs client = Discogs.newKeySecretAuthenticatedInstance(
              "My consumer/app key",
              "My consumer/app secret", 
              "MyUserAgent/1.0");
      
      // Access APIs
      SearchRequest request = SearchRequest.builder()
              .artist("Dream Theater")
              .build();
      DatabaseApi databaseApi = client.getDatabaseApi();
      SearchResponse response = databaseApi.search(request);
      ```
   3. Personal access token that can access user resources only:
      ```java
      Discogs client = Discogs.newTokenAuthenticatedInstance(
              "My personal access token",
              "MyUserAgent/1.0");
      
      // Access APIs
      UserIdentityApi userIdentityApi = client.getUserIdentityApi();
      AuthenticatedUser userInfo = userIdentityApi.getAuthenticatedUser();
      ```
   4. OAuth to access resources on behalf of a user:
      ```java
      // Will prompt the user to grant access
      Discogs client = Discogs.newOAuthInstance(
              new KeySecretAuthInfo("My App Key", "My App Secret"),
              "MyUserAgent/1.0",
              8095); // local port for OAuth callback
      
      // Fetch user OAuth token for persistence
      OAuthInfo authInfo = (OAuthInfo) oAuthClient.getConnection()
              .getAuthManager()
              .getAuthInfo();
      ```
   5. OAuth with existing persisted user token to access resources on beahlf of a user:
      ```java
      Discogs client = Discogs.newOAuthInstance(
              new KeySecretAuthInfo("My App Key", "My App Secret"),
              "MyUserAgent/1.0",
              8095, // local port for OAuth callback
              new OAuthInfo("User Token", "User Token Secret"));
      
      // Access APIs
      UserIdentityApi userIdentityApi = client.getUserIdentityApi();
      AuthenticatedUser userInfo = userIdentityApi.getAuthenticatedUser();
      ```

<div align="right">(<a href="#readme-top">back to top</a>)</div>

<a name="recipes"></a>
## Recipes
### Searching for content
```java
DatabaseApi databaseApi = client.getDatabaseApi();
SearchResponse artistSearchResults = databaseApi.search(
        SearchRequest.builder()
                .artist("Dream Theater")
                .build());

SearchResponse releaseSearchResults = databaseApi.search(
        SearchRequest.builder()
                .artist("Babymetal")
                .title("Metal Galaxy")
                .build());
```

### Adding a release to a user's collection folder
```java
DatabaseApi databaseApi = client.getDatabaseApi();
SearchResponse searchResponse = databaseApi.search(
        SearchRequest.builder()
                .artist("Babymetal")
                .title("Metal Galaxy")
                .build());

UserCollectionApi userCollectionApi = client.getUserCollectionApi();
CreateFolderResponse createFolderResponse = userCollectionApi.createFolder(
        CreateFolderRequest.builder()
                .username("My Username")
                .name("My Folder")
                .build());
AddToFolderResponse addToFolderResponse =
        userCollectionApi.addToFolder(AddToFolderRequest.builder()
                .username("My Username")
                .releaseId(searchResponse.getResults().getFirst().getId())
                .folderId(createFolderResponse.getId())
                .build());
```

### Customizing the HTTP client configuration

<details>
<summary>OkHttpClientBuilder example</summary>

If your use-case requires configuring the underlying <code>OkHttpClient</code> instance (e.g., configuring your own
SSL cert verification, proxy, and/or connection timeouts), you can configure the client with the provided
[OkHttpClientBuilder](https://github.com/andy-miles/discogs-java-client/blob/main/src/main/java/com/amilesend/omdb/client/connection/http/OkHttpClientBuilder.java),
or alternatively with [OkHttp's builder](https://square.github.io/okhttp/4.x/okhttp/okhttp3/-ok-http-client/).

```java
OkHttpClient httpClient = OkHttpClientBuilder.builder()
        .trustManager(myX509TrustManager) // Custom trust manager for self/internally signed SSL/TLS certs
        .hostnameVerifier(myHostnameVerifier) // Custom hostname verification for SSL/TLS endpoints
        .proxy(myProxy, myProxyUsername, myProxyPassword) // Proxy config
        .connectTimeout(8000L) // connection timeout in milliseconds
        .readTimeout(5000L) // read timeout in milliseconds
        .writeTimeout(5000L) // write timeout in milliseconds
        .build();
DiscogsConnectionBuilder connection = new DiscogsConnectionBuilder()
        .httpClient(httpClient)
        .authManager(new AuthManagerFactory().newUnauthenticatedAuthManager())
        .gsonFactory(new GsonFactory())
        .isGzipContentEncodingEnabled(true)
        .userAgent("MyUserAgent/1.0")
        .build();

Discogs client = new Discogs(connection);
```

</details>


<div align="right">(<a href="#readme-top">back to top</a>)</div>

<!-- CONTRIBUTING -->
## Contributing

If you have a suggestion that would make this better, please fork the repo and create a pull request. You can also open an issue with the tag "enhancement".
Don't forget to give the project a star! Thanks again!

1. Fork the Project
2. Create your Feature Branch (`git checkout -b feature/MyFeature`)
3. Commit your Changes (`git commit -m 'Add my feature'`)
4. Push to the Branch (`git push origin feature/MyFeature`)
5. Open a Pull Request

<div align="right">(<a href="#readme-top">back to top</a>)</div>

<!-- LICENSE -->
## License

Distributed under the GPLv3 license. See [LICENSE](https://github.com/andy-miles/discogs-java-client/blob/main/LICENSE) for more information.

<div align="right">(<a href="#readme-top">back to top</a>)</div>


<!-- CONTACT -->
## Contact

Andy Miles - andy.miles (at) amilesend.com

Project Link: [https://github.com/andy-miles/discogs-java-client](https://github.com/andy-miles/discogs-java-client)

<div align="right">(<a href="#readme-top">back to top</a>)</div>


<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
<!-- MARKDOWN LINKS & IMAGES -->
<!-- https://www.markdownguide.org/basic-syntax/#reference-style-links -->
[contributors-shield]: https://img.shields.io/github/contributors/andy-miles/discogs-java-client.svg?style=for-the-badge
[contributors-url]: https://github.com/andy-miles/discogs-java-client/graphs/contributors
[forks-shield]: https://img.shields.io/github/forks/andy-miles/discogs-java-client.svg?style=for-the-badge
[forks-url]: https://github.com/andy-miles/discogs-java-client/network/members
[stars-shield]: https://img.shields.io/github/stars/andy-miles/discogs-java-client.svg?style=for-the-badge
[stars-url]: https://github.com/andy-miles/discogs-java-client/stargazers
[issues-shield]: https://img.shields.io/github/issues/andy-miles/discogs-java-client.svg?style=for-the-badge
[issues-url]: https://github.com/andy-miles/discogs-java-client/issues
[license-shield]: https://img.shields.io/github/license/andy-miles/discogs-java-client.svg?style=for-the-badge
[license-url]: https://github.com/andy-miles/discogs-java-client/blob/master/LICENSE.txt
[linkedin-shield]: https://img.shields.io/badge/-LinkedIn-black.svg?style=for-the-badge&logo=linkedin&colorB=555
[linkedin-url]: https://www.linkedin.com/in/andy-miles
[product-screenshot]: images/screenshot.png
[Next.js]: https://img.shields.io/badge/next.js-000000?style=for-the-badge&logo=nextdotjs&logoColor=white
[Next-url]: https://nextjs.org/
[React.js]: https://img.shields.io/badge/React-20232A?style=for-the-badge&logo=react&logoColor=61DAFB
[React-url]: https://reactjs.org/
[Vue.js]: https://img.shields.io/badge/Vue.js-35495E?style=for-the-badge&logo=vuedotjs&logoColor=4FC08D
[Vue-url]: https://vuejs.org/
[Angular.io]: https://img.shields.io/badge/Angular-DD0031?style=for-the-badge&logo=angular&logoColor=white
[Angular-url]: https://angular.io/
[Svelte.dev]: https://img.shields.io/badge/Svelte-4A4A55?style=for-the-badge&logo=svelte&logoColor=FF3E00
[Svelte-url]: https://svelte.dev/
[Laravel.com]: https://img.shields.io/badge/Laravel-FF2D20?style=for-the-badge&logo=laravel&logoColor=white
[Laravel-url]: https://laravel.com
[Bootstrap.com]: https://img.shields.io/badge/Bootstrap-563D7C?style=for-the-badge&logo=bootstrap&logoColor=white
[Bootstrap-url]: https://getbootstrap.com
[JQuery.com]: https://img.shields.io/badge/jQuery-0769AD?style=for-the-badge&logo=jquery&logoColor=white
[JQuery-url]: https://jquery.com 
