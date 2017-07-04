import { MaestroToolsPage } from './app.po';

describe('travel-ci App', () => {
  let page: MaestroToolsPage;

  beforeEach(() => {
    page = new MaestroToolsPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
