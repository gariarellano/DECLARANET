import { NgModule } from "@angular/core";
import { HttpClientModule, HttpHeaders } from "@angular/common/http";
import { Apollo, ApolloModule } from "apollo-angular";
import { HttpLinkModule, HttpLink } from "apollo-angular-link-http";
import { InMemoryCache } from "apollo-cache-inmemory";
import { environment } from "src/environments/environment";
import { DefaultOptions } from "apollo-client";

@NgModule({
  declarations: [],
  imports: [HttpClientModule, ApolloModule, HttpLinkModule],
})
export class GraphQLModule {
  private readonly uriOmext: string = `${environment.API_BASE}`;
  // private readonly uriPerfilamiento: string = `${environment.API_PERFILAMIENTO}`;


  constructor(apollo: Apollo, httpLink: HttpLink) {
    const options1: any = { uri: this.uriOmext };

    const defaultOptions: DefaultOptions = {
      watchQuery: {
        fetchPolicy: 'no-cache',
        errorPolicy: 'ignore',
      },
      query: {
        fetchPolicy: 'no-cache',
        errorPolicy: 'all',
      },
    };

    apollo.createNamed("servidores", {
      link: httpLink.create(options1),
      cache: new InMemoryCache({
        addTypename: false
      }),
      defaultOptions
    });

    // const options2: any = { uri: this.uriPerfilamiento };
    // apollo.createNamed("perfilamiento", {
    //   link: httpLink.create(options2),
    //   cache: new InMemoryCache({
    //     addTypename: false
    //   })
    // });
  }
}
