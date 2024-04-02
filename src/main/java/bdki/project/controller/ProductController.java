// package bdki.project.controller;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.http.HttpStatus;
// import org.springframework.http.ResponseEntity;
// import org.springframework.web.bind.annotation.*;

// import bdki.project.entity.Product;
// import bdki.project.services.ProductServices;

// @RestController
// @RequestMapping("/api/product")
// public class ProductController {

//     private final ProductServices productServices;

//     @Autowired
//     public ProductController(ProductServices productServices) {
//         this.productServices = productServices;
//     }

//     @PostMapping("/create")
//     public ResponseEntity<Product> createProduct(@RequestBody Product product) {
//         Product newProduct = productServices.createProduct(product);
//         return new ResponseEntity<>(newProduct, HttpStatus.OK);
//     }

//     @GetMapping("/{productId}")
//     public ResponseEntity<Product> getProductById(@PathVariable("productId") long productId) {
//         Product product = productServices.getProductById(productId);
//         return new ResponseEntity<>(product, HttpStatus.OK);
//     }

//     @PutMapping("/{productId}")
//     public ResponseEntity<Product> updateProduct(@PathVariable("productId") long productId, @RequestBody Product product) {
//         Product updatedProduct = productServices.updateProduct(productId, product);
//         return new ResponseEntity<>(updatedProduct, HttpStatus.OK);
//     }

//     @DeleteMapping("/{productId}")
//     public ResponseEntity<String> deleteProduct(@PathVariable("productId") long productId) {
//         productServices.deleteProduct(productId);
//         return new ResponseEntity<>("Product berhasil dihapus: " + productId, HttpStatus.OK);
//     }
// }
