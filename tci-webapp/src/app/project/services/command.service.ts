import { Injectable } from '@angular/core';
import {Http, Headers, RequestOptions, Response} from "@angular/http";
import {Command} from "../models/command";
import 'rxjs/add/operator/toPromise';
import {environment} from "../../../environments/environment";

@Injectable()
export class CommandService {

  constructor(private http: Http) {}

  addNewCommands(commands: Command[]): Promise<Command[]> {

    let headers = new Headers({ 'Content-Type': 'application/json' });
    let options = new RequestOptions({ headers: headers });

    return this.http.post('/api/commands', commands, options)
      .toPromise()
      .then((res : Response) => res.json() as Command[])
      .catch((err: Error) => this.handleError(err));
  }

  handleError(err: Error): Promise<Command[]> {

    if(environment.enableDebug)
      console.error(err);

    return Promise.reject(err.message || err);
  }
}
