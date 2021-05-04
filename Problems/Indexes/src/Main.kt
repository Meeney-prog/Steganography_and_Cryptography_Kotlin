fun solution(products: List<String>, product: String) =
        print(products.indices.filter { products[it] == product }.joinToString(" "))