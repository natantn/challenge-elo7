# Elo7 Challenge - Space Explorer

Enunciado do exercício

[Desafio de Programação Estágio / Jr](https://gist.github.com/elo7-developer/f0b91a7a98e5e65288b875ac6d376875)

---

# Sobre a Solução

O projeto foi desenvolvido utilizando

- Spring Boot
- MongoDB (via MongoDB Cloud)

Para execução do projeto, é necessário popular o env.properties-sample com os valores informados

É possível interagir com a API a partir da seguinte collection no Postman

[Elo7 - Space Explorers.postman_collection.json](https://drive.google.com/file/d/1Jnq55buhOceWr3444KHoMyAUJMakOIgz/view?usp=sharing)

---

# Explorer

Uma sonda pode ser criada a partir de um nome e deve ter um atributo *slug* único que servirá de identificação perante outras sondas. Toda sonda criada permanece na base.

Para que uma sonda possa explorar outro planeta é necessário que ela esteja na base e que haja um planeta descoberto alvo junto das coordenadas desejadas de seu pouso, com isso é possível fazer seu lançamento.

- Caso a coordenada não esteja disponível, a sonda irá pousa na próxima coordenada livre ao redor no sentido horário das direções, ainda orientada para a direção informada.
- Caso não haja coordenada livre, a sonda deve retornar para a base.

Para que uma sonda possa se mover em um planeta, é necessário informar uma sequencia de comandos de movimento. A sonda deve executar todo movimento identificável possível para ela. A sonda só deve se mover para coordenadas livres, isto é, que estejam dentro dos limites do planeta e que não esteja ocupada por nenhuma outra sonda.

- Caso um comando de movimento não seja identificado, ele deve ser ignorado
- Caso um comando de movimento não seja possível de ser realizado, ele deve ser ignorado.

## Recursos e endpoints

### Create New Explorer

`POST - /explorer`

- Request

```json
{
    "name" : "alpha"
}
```

- HTTP 200

```json
{
    "name": "alpha",
    "slug": "alpha20220913",
    "posX": -1,
    "posY": -1,
    "actualPlanet": null,
    "orientation": null,
    "status": "AT_BASE"
}
```

- HTTP 404

```json
{
    "message": "There is one explorer already created with this name. Please, try another one",
    "registerType": "ERROR",
    "register": {
        "name": "alpha",
        "slug": "alpha20220913",
        "posX": -1,
        "posY": -1,
        "actualPlanet": null,
        "orientation": null,
        "status": "AT_BASE"
    }
}
```

### List All Explorers

`GET - /explorer`

- HTTP 200

```json
[
    {
        "name": "alpha",
        "slug": "alpha20220913",
        "posX": 2,
        "posY": 2,
        "actualPlanet": {
            "name": "Saturn",
            "slug": "saturn20220914",
            "coordenates": "3x3"
        },
        "orientation": "North",
        "status": "ON_PLANET"
    },
    {
        "name": "beta",
        "slug": "beta20220914",
        "posX": -1,
        "posY": -1,
        "actualPlanet": null,
        "orientation": null,
        "status": "AT_BASE"
    }
]
```

### Get An Explorer By Slug

`GET - /explorer/{slug}`

- HTTP 200

```json
{
    "name": "alpha",
    "slug": "alpha20220913",
    "posX": -1,
    "posY": -1,
    "actualPlanet": null,
    "orientation": null,
    "status": "AT_BASE"
}
```

- HTTP 204

```json
{
    "message": "There is no explorer identified by informed slug. Please, verify it",
    "registerType": "ERROR",
    "register": null
}
```

### Launching An Explorer

`PUT - /explorer/{slug}/launch`

- Request

```json
{
    "planetSlug" : "saturn20220914",
    "posX" : 2,
    "posY" : 2,
    "orientation" : "North"
}
```

- HTTP 200

```json
{
    "name": "alpha",
    "slug": "alpha20220913",
    "posX": 2,
    "posY": 2,
    "actualPlanet": {
        "name": "Saturn",
        "slug": "saturn20220914",
        "coordenates": "3x3"
    },
    "orientation": "North",
    "status": "ON_PLANET"
}
```

- HTTP 400

```json
{
    "message": "It's not possible to land in target position neither any position around \nPlease, try again",
    "registerType": "EXPLORER",
    "register": {
        "name": "omega",
        "slug": "omega20220914",
        "posX": -1,
        "posY": -1,
        "actualPlanet": null,
        "orientation": null,
        "status": "AT_BASE"
    }
}
```

### Move An Explorer

`PUT - /explorer/{slug}/move`

- Request

```json
{
    "movement" : "MMRMMRMRRML"
}
```

- HTTP 200
- HTTP 400

```json
{
    "message": "Explorer at base. It's not possible to move it around",
    "registerType": "EXPLORER",
    "register": {
        "name": "omega",
        "slug": "omega20220914",
        "posX": -1,
        "posY": -1,
        "actualPlanet": null,
        "orientation": null,
        "status": "AT_BASE"
    }
}
```

# Planet

Um planeta pode ser descoberto a qualquer momento e registrado a partir de um nome e um atributo *slug* único que servirá de identificação perante outros planetas. Após registrado, um planeta deve manter a informação de quais sondas estão em sua superfície

Todo planeta tem um mapa de coordenadas de $m$ x $n$ posições. Sendo que

- $m \geq 3 \quad{\wedge}\quad n \leq 10$
- $n \geq 3 \quad{\wedge}\quad n \leq 10$

Nesse mapa de coordenadas é possível pousos e movimentações de sondas. Não é possível que mais de uma sonda ocupe uma mesma posição simultaneamente.

Um planeta pode ter no máximo $m \times n$ sondas.

## Recursos e endpoints

### Register a New Planet

`POST - /planet`

- Request

```json
{
    "name" : "Saturn",
    "limitX" : 3,
    "limitY" : 3
}
```

- HTTP 200

```json
{
    "name": "Saturn",
    "slug": "saturn20220914",
    "coordenates": "3x3",
    "explorersAtPlanet": []
}
```

- HTTP 404

```json
{
    "message": "There is one explorer already created with this name. Please, try another one",
    "registerType": "PLANET",
    "register": {
        "name": "Saturn",
        "slug": "saturn20220914",
        "coordenates": "3x3",
        "explorersAtPlanet": []
    }
}
```

### List All Planets Discovered

`GET - /planet`

- HTTP 200

```json
[
    {
        "name": "Saturn",
        "slug": "saturn20220914",
        "coordenates": "3x3",
        "explorersAtPlanet": [
            {
                "name": "alpha",
                "slug": "alpha20220914",
                "coordenates": "1x1",
                "orientation": "East"
            }
        ]
    }
]
```

### Get a Planet by Slug

`GET - /planet/{slug}`

- HTTP 200

```json
{
    "name": "Saturn",
    "slug": "saturn20220914",
    "coordenates": "3x3",
    "explorersAtPlanet": []
}
```

- HTTP 204

```json
{
    "message": "There is no planet identified by informed slug. Please, verify it",
    "registerType": "ERROR",
    "register": null
}
```