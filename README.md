# ShopGUIPrototype
Shop GUI prototype using JSwing and Sqlite database.

# Run
To run the code, download the project and open in an IDE like intelliJ IDEA. Make sure sqlite-jbdbc is 
installed and the path is set up properly in your IDE.

# Documentation
## GUI and User Story (Design)
When the user first starts the program they are prompted to enter in a valid username (>= 20 chars), and to select whether 
they are a seller or not. If they are a seller, they check the box. Then click login and if the box is checked then the seller
interface is loaded. If not, then the buyer interface is loaded.

There are a few similarities and key differences between the two user interfaces. 

In the buyer interface, all listed products being sold are listed with an order button at the bottom of each product panel. 
Located on the right side of the interface is an empty panel which will house the product order menu. Finally, at the bottom of 
the interface is a region where past orders are listed. The user can decide to see only the orders which are linked to their entered 
username. For example, if the user used that username to order an item, it will be listed there, or if the user is selling something 
that has been ordered. 

When the user finds a listed product they would like to order, they can hit the order button and the order menu panel will be 
filled. The user can then adjust the quantity of items they would like to order (the spinner will not increment past the number of 
items actually in stock). When they are ready to order the items, they can simply hit the order button. The order button will create 
a new order, add it to the database, refresh the list of products to correctly reflect the amount in stock (if the item is out of 
stock it will be removed entirely from the list), and then refresh the list of current orders.

In the seller interface, roughly the same functionality is provided by the product list panel and the orders list panel, 
except that the user cannot order items (that must be done in the buyer panel). On the right side of the screen, the user will 
find a panel with a few empty prompts for adding a new product to the store, including the product name, product price, and the 
quantity of items they would like to have in stock. When the product is ready to be added, the user can hit the Add product button 
and the item will be added to the database, then the products list will be refreshed to accurately depict the store.

## Implementation
When considering how I wanted to set up the structure of my code, I decided that I wanted my file structure/class implementation 
to tell a clear story of how the program would progress. In the Main class, I first create a new instance of MainFrame class which 
extends JFrame. This will be used as the root frame for the entirety of the program’s execution. Inside of the MainFrame class, I 
start to populate the frame with panels that will be used for the login screen. When the panels are added in the desired position 
within the frame, I create a new instance of the LoginPanel class which will add in the components of the login screen, like the 
username prompt, the username textfield, the login button, and the seller checkbox. All functionality for logging in is contained 
in this class as functions. The login button event listener is implemented as a lambda function for simplicity. When the user logs 
in, entire loginpanel and its parent panels are removed and the correct panel is added in depending on whether the user selected 
seller or not. If the user selects seller, then a new instance of the SellerPanel is created. The SellerPanel class when initialized
will create a panel for displaying all products in the shop, a panel for displaying the orders, and a panel for adding new products.

For displaying the products, an instance of the SellerProductsPanel class is created. It uses a GridLayout layout. It then 
accesses the database and retrieves the products from the Products table. For each product it creates an instance of the 
Product class which is used to render all of the labels and the order button for its product. The products list is also vertically 
scrollable.

For displaying the orders, an instance of the OrderDisplayPanel class is created. This class is used by both the buyer and 
seller interfaces. The OrderDisplayPanel class works similarly to the SellerProductsPanel, it populates the panel with orders
retrieved from the Orders datatable. For each order, an instance of the Order class is created. The Order class creates the 
labels for the order display.

For adding products, the SellProductMenu panel is simply populated with the necessary labels and text field components, 
along with a JButton which has an event listener. When the event listener is activated by the button press, the addProduct() 
function is run which inserts a new record into the Products table, then it refreshes the products panel.

If the user does not select seller, then a new instance of the BuyerPanel is created. The BuyerPanel class will instantiate 
the BuyProductMenu class, the BuyerProductsPanel class, and the OrdersDisplayPanel class.

The BuyerProductsPanel class works in the same way that the SellerProductsPanel class works, except that each product has its 
own instance of BuyerProduct which displays the same information as the SellerProduct class with an additional order button. 
The order button calls a function on the instance of the BuyProductMenu.

The BuyProductMenu, when instantiated, is blank. When the order button from an instance of the BuyerProduct class is pressed,
the product menu panel is populated with the information pertaining to the selected product, along with an instance of JSpinner
for the order quantity, and a JTextfield for the shipping address. Finally, an order button is added which adds the item to the 
Orders table in the database, which also includes the name of the seller and the buyer plus all of the information provided in the 
product menu. It refreshes the list of products to accurately depict the amount left in stock by calling the displayProducts() 
function on the BuyerProductsPanel class. 

The OrdersDisplayPanel class for the seller story is the same class that is used in the buyer story, so I won’t explain it again.

## Testing
For testing I used use case testing during the development of the program. Once a class was found to function as expected I 
moved on to the next part of the user story until the program was written. I first tested the login story, which is explained 
in the Graphical Interface section. Then the seller story, which consisted of testing the seller products list to make sure it 
accurately displayed the items being sold. Then the products adding menu to make sure no errors were returned and different 
inputs could be safely handled. Finally, the orders list had to accurately show all of the orders, or when the user chooses 
to only show orders that relate to them, it should only shows those respective orders. For the buyer story, when an order button 
is clicked on a product panel, all of the information on the buying product menu needs to be accurately updated. When the user 
chooses to confirm the order in the buying product menu, the information should disappear and the products list should be 
accurately updated, along with the orders list, which needs to have the same functionality as in the seller story. All of 
these things were tested.
