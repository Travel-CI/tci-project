import { TciWebappPage } from './app.po';

describe('tci-webapp App', () => {
  let page: TciWebappPage;

  beforeEach(() => {
    page = new TciWebappPage();
  });

  it('should display message saying app works', () => {
    page.navigateTo();
    expect(page.getParagraphText()).toEqual('app works!');
  });
});
