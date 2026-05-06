import { NgModule } from '@angular/core';
import { HttpClientModule, HttpHeaders } from '@angular/common/http';
import { Apollo, ApolloModule } from 'apollo-angular';
import { HttpLinkModule, HttpLink } from 'apollo-angular-link-http';
import { InMemoryCache } from 'apollo-cache-inmemory';
import { environment } from 'src/environments/environment';

@NgModule({
  imports: [
    HttpClientModule,
    ApolloModule,
    HttpLinkModule
  ]
})
export class GraphQLModule {

  private readonly uriDeclaracion: string = `${environment.API_BASE}/validarDeclaracion`;
  private readonly uriEntes: string = `${environment.API_ENTES}`;

  constructor(
    apollo: Apollo,
    httpLink: HttpLink
  ) {
    const options1: any = { uri: this.uriDeclaracion };
    apollo.createDefault({
      link: httpLink.create(options1),
      cache: new InMemoryCache()
    });

    const options2: any = { uri: this.uriEntes };
    apollo.createNamed('entes', {
      link: httpLink.create(options2),
      cache: new InMemoryCache()
    });

    // const options3: any = { uri: this.uriAviso };
    // apollo.createNamed('aviso', {
    //   link: httpLink.create(options3),
    //   cache: new InMemoryCache()
    // });
  }
}


// const uri = `${environment.API_BASE}/validarDeclaracion`; // <-- add the URL of the GraphQL server here
// const uriEntes = `${environment.API_ENTES}`; // <-- add the URL of the GraphQL server here
// export function createApollo(httpLink: HttpLink) {
//   return {
//     link: httpLink.create({uri}),
//     cache: new InMemoryCache(),
//   };
// }

// @NgModule({
//   exports: [ApolloModule, HttpLinkModule],
//   providers: [
//     {
//       provide: APOLLO_OPTIONS,
//       useFactory: createApollo,
//       deps: [HttpLink],
//     },
//   ],
// })
// export class GraphQLModule {}
