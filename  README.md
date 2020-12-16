Run application : 

first config a h2 base : jdbc:h2:mem:db-users
you can change the configuration in application.properties

run application using maven ; mvn spring-boot:run

1 - System for authentication of diffrent user in a system :

  package : entities ->
                       - Utilisateur : class abstract 
                       - Etudiant : class that inhertnce from Utilisateur and has cne as a spesific attribut
                       - Prof : class that inhertnce from Utilisateur and has cin as a spesific attribut
                       - Admin : class that inhertnce from Utilisateur and has cin as a spesific attribut
            
            For communicate to data base I am using JPA as ORM for mapping the object in our database.
            
          : repo    ->
                     - UtilisateurRepository : interface extend from JpaRepository
         
         
          : security -> 
                  WebSecurityConfig : class contain the configuration for all the app 
                 
                filters -> JwtAuthFilter : filter that intercept our request and validate the token
                jwt     -> - JwtConfig : class that containe the configuration of our jwt
                            - JwtUtil   : class that conatine method to verifier and extract data from the token

                services -> ApplicationUserService : class implements UserDetailsService and override method loadUserByUsername and return authenticated User(spring object)
                  
                       