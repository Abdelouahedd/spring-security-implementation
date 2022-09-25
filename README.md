<p align="center">
  <img alt="Github top language" src="https://img.shields.io/github/languages/top/Abdelouahedd/spring-security-implementation?color=56BEB8">

  <img alt="Github language count" src="https://img.shields.io/github/languages/count/Abdelouahedd/spring-security-implementation?color=56BEB8">

  <img alt="Repository size" src="https://img.shields.io/github/repo-size/Abdelouahedd/spring-security-implementation?color=56BEB8">

  <img alt="License" src="https://img.shields.io/github/license/Abdelouahedd/spring-security-implementation?color=56BEB8">

  <img alt="Github issues" src="https://img.shields.io/github/issues/Abdelouahedd/spring-security-implementation?color=56BEB8" /> 

   <img alt="Github forks" src="https://img.shields.io/github/forks/Abdelouahedd/spring-security-implementation?color=56BEB8" /> 

   <img alt="Github stars" src="https://img.shields.io/github/stars/Abdelouahedd/spring-security-implementation?color=56BEB8" /> 
</p>
<p align="center">
  <a href="#dart-about">About</a> &#xa0; | &#xa0; 
  <a href="#sparkles-features">Features</a> &#xa0; | &#xa0;
  <a href="#rocket-technologies">Technologies</a> &#xa0; | &#xa0;
  <a href="#white_check_mark-requirements">Requirements</a> &#xa0; | &#xa0;
  <a href="#checkered_flag-starting">Starting</a> &#xa0; | &#xa0;
  <a href="#memo-license">License</a> &#xa0; | &#xa0;
  <a href="#contribute-contributions">Contributions</a> &#xa0; | &#xa0;
  <a href="https://github.com/Abdelouahedd" target="_blank">Author</a>
</p>
<br>

## :dart: About ##

The project is a small implementation of securing a REST api using spring security using Jwt

## :sparkles: Features ##

:heavy_check_mark: Feature 1; Configure the application with embedded Data base (H2), and create the entities with
repository <br/>
:heavy_check_mark: Feature 2; Create a controller userController <br/>
:heavy_check_mark: Feature 3; Create filter CustomAuthenticationFilter for the authentication part <br/>
:heavy_check_mark: Feature 4; Create filter JwtAuthFilter for the authorization part <br/>

package :
For communicate to database I am using JPA as ORM for mapping the object in our database.

- entities -><br>
  - User : This class contains information about the users of the application.<br>
  - Role : This class contains roles <br>


- repo -><br>
  - UserRepository : interface extend from JpaRepository<br>
  - RoleRepository : interface extend from JpaRepository<br>

- security -> <br>
  - WebSecurityConfig : class contain the configuration of security for the application, in new version of Spring
    security we can configure without extend WebSecurityConfigurerAdapter
    we can just create Bean of type SecurityFilterChain and add rules of permit or deny access to Http request<br>

- filters -> <br>
  - JwtAuthFilter : Filter that intercept our request and validate the token and parse it for create a user information
    in SecurityContextHolder<br>
  - CustomAuthenticationFilter : This filter is used in the login phase , It automatically calls
    UserDetailsService.loadUserByUsername, and if the user exists,
    it creates and returns two JWT tokens: one is the access token, used to authorize the user, the other is the refresh
    token, used by the client to acquire
    a new access token without having to login again. <br>
- jwt -><br>
  - JwtConfig : class that contain the configuration of our jwt<br>
  - JwtUtil   : class that content method to verifier and extract data from the token<br>

- services -> <br>
  - ApplicationUserService : class implements UserDetailsService and override method loadUserByUsername and return
    authenticated User(spring object)<br>


- controllers -><br>
  - UsersController : controller that contain 2 end points : <br>
  - get("/") is for all users can access<br>
  - get("/api/users") get all users in database and must login user has a admin role<br>

- RolesController -><br>
  - RolesController : controller that contain end points of management roles : <br>
  - get("/api/roles") is for all users can access<br>
  - post("/api/roles") add new role to database and must login user has an admin role<br>

## :rocket: Technologies ##

The following tools were used in this project:

- [Java](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html)
- [Spring](https://spring.io/)
- [Spring security](https://spring.io/projects/spring-security)

## :white_check_mark: Requirements ##

Before starting :checkered_flag:, you need to have [Git](https://git-scm.com)
and [Java](https://www.oracle.com/java/technologies/javase/jdk11-archive-downloads.html) and [curl](https://curl.se/) installed.

## :checkered_flag: Starting ##

```bash
# Clone this project
$ git clone https://github.com/Abdelouahedd/spring-security-implementation

# Access
$ cd spring-security-implementation

# Install dependencies
$ ./mvnw clean compile

# Run the project
$ ./mvnw spring-boot:run

# The server will initialize in the <http://localhost:9000>
## for test you can use admin account 
curl -i -X POST \
-d  '{"email":"Admin@hotmail.com","password":"admin1234"}' \
http://localhost:9000/login | grep access_token
## or user account
curl -i -X POST \
-d  '{"email":"user@hotmail.com","password":"user1234"}' \
http://localhost:9000/login | grep access_token
```

## :memo: License ##

This project is under license from MIT. For more details, see the [LICENSE](LICENSE.md) file.

Made with :heart: by <a href="https://github.com/Abdelouahedd/spring-security-implementation" target="_blank">
Abdelouahed</a>

&#xa0;

## :contribute: Contributions ##

If it's your first contribution to a public Github repo
follow [this](https://github.com/firstcontributions/first-contributions).

Otherwise Just

- Pick an issue.
- Check if someone is already working on it (read the comments)
- Let everyone know that you are working on the issue (Add a comment expressing that)

Thank you for your contribution.

<a href="#top">Back to top</a>
