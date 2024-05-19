-- MySQL dump 10.13  Distrib 8.3.0, for Linux (x86_64)
--
-- Host: localhost    Database: omess
-- ------------------------------------------------------
-- Server version	8.3.0

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Current Database: `omess`
--

CREATE DATABASE /*!32312 IF NOT EXISTS*/ `omess` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;

USE `omess`;

--
-- Table structure for table `SPRING_SESSION`
--

DROP TABLE IF EXISTS `SPRING_SESSION`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SPRING_SESSION` (
  `PRIMARY_ID` char(36) NOT NULL,
  `SESSION_ID` char(36) NOT NULL,
  `CREATION_TIME` bigint NOT NULL,
  `LAST_ACCESS_TIME` bigint NOT NULL,
  `MAX_INACTIVE_INTERVAL` int NOT NULL,
  `EXPIRY_TIME` bigint NOT NULL,
  `PRINCIPAL_NAME` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`PRIMARY_ID`),
  UNIQUE KEY `SPRING_SESSION_IX1` (`SESSION_ID`),
  KEY `SPRING_SESSION_IX2` (`EXPIRY_TIME`),
  KEY `SPRING_SESSION_IX3` (`PRINCIPAL_NAME`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SPRING_SESSION`
--

LOCK TABLES `SPRING_SESSION` WRITE;
/*!40000 ALTER TABLE `SPRING_SESSION` DISABLE KEYS */;
/*!40000 ALTER TABLE `SPRING_SESSION` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `SPRING_SESSION_ATTRIBUTES`
--

DROP TABLE IF EXISTS `SPRING_SESSION_ATTRIBUTES`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `SPRING_SESSION_ATTRIBUTES` (
  `SESSION_PRIMARY_ID` char(36) NOT NULL,
  `ATTRIBUTE_NAME` varchar(200) NOT NULL,
  `ATTRIBUTE_BYTES` blob NOT NULL,
  PRIMARY KEY (`SESSION_PRIMARY_ID`,`ATTRIBUTE_NAME`),
  CONSTRAINT `SPRING_SESSION_ATTRIBUTES_FK` FOREIGN KEY (`SESSION_PRIMARY_ID`) REFERENCES `SPRING_SESSION` (`PRIMARY_ID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci ROW_FORMAT=DYNAMIC;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `SPRING_SESSION_ATTRIBUTES`
--

LOCK TABLES `SPRING_SESSION_ATTRIBUTES` WRITE;
/*!40000 ALTER TABLE `SPRING_SESSION_ATTRIBUTES` DISABLE KEYS */;
/*!40000 ALTER TABLE `SPRING_SESSION_ATTRIBUTES` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api`
--

DROP TABLE IF EXISTS `api`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `domain_id` bigint NOT NULL,
  `path` varchar(20) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  `endpoint` varchar(2000) NOT NULL,
  `method` varchar(10) NOT NULL,
  `request_schema` text,
  `response_schema` text,
  `status_code` smallint NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `domain_id` (`domain_id`),
  CONSTRAINT `api_ibfk_1` FOREIGN KEY (`domain_id`) REFERENCES `domain` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api`
--

LOCK TABLES `api` WRITE;
/*!40000 ALTER TABLE `api` DISABLE KEYS */;
INSERT INTO `api` VALUES (4,10,'P14/A21/D10/A4','회원가입','이름, 이메일, 비밀번호로 회원가입','/api/v1/members/signup','POST','','{\n}',200,'2024-05-13 21:36:18','2024-05-13 21:36:18'),(5,10,'P14/A21/D10/A5','로그인','d','/api/v1/members/signin','POST','','{}',200,'2024-05-13 21:36:59','2024-05-13 21:36:59'),(6,11,'P14/A21/D11/A6','게시글 생성','a','/api/boards','POST','','{}',200,'2024-05-13 21:37:38','2024-05-13 21:37:38'),(7,12,'P14/A21/D12/A7','채팅방 생성','/api/v1/chat','1','POST','','{}',200,'2024-05-13 21:38:08','2024-05-13 21:38:08'),(10,19,'P27/A32/D19/A10','API 수정','단일 API 수정을 위한 api입니다.','/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}/domains/{domainId}/apis/{apiId}','PUT','{\n    \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n    \"title\": \"UpdateApiRequest\",\n    \"type\": \"object\",\n    \"properties\": {\n        \"method\": {\n            \"type\": \"string\",\n            \"minLength\": 1,\n            \"maxLength\": 10\n        },\n        \"name\": {\n            \"type\": \"string\",\n            \"minLength\": 1,\n            \"maxLength\": 90\n        },\n        \"description\": {\n            \"type\": \"string\",\n            \"minLength\": 1,\n            \"maxLength\": 50\n        },\n        \"endpoint\": {\n            \"type\": \"string\",\n            \"minLength\": 1,\n            \"maxLength\": 2000\n        },\n        \"statusCode\": {\n            \"type\": \"number\",\n            \"minimum\": 100,\n            \"maximum\": 599\n        },\n        \"requestSchema\": {\n            \"type\": [\n                \"string\",\n                \"null\"\n            ]\n        },\n        \"responseSchema\": {\n            \"type\": [\n                \"string\",\n                \"null\"\n            ]\n        },\n        \"updateRequestHeaderRequests\": {\n            \"type\": \"array\",\n            \"items\": {\n                \"type\": \"object\",\n                \"properties\": {\n                    \"headerKey\": {\n                        \"type\": \"string\",\n                        \"minLength\": 1,\n                        \"maxLength\": 50\n                    },\n                    \"headerValue\": {\n                        \"type\": \"string\",\n                        \"minLength\": 1,\n                        \"maxLength\": 100\n                    }\n                },\n                \"required\": [\n                    \"headerKey\",\n                    \"headerValue\"\n                ]\n            }\n        },\n        \"updateQueryParamRequests\": {\n            \"type\": \"array\",\n            \"items\": {\n                \"type\": \"object\",\n                \"properties\": {\n                    \"name\": {\n                        \"type\": \"string\",\n                        \"minLength\": 1,\n                        \"maxLength\": 20\n                    },\n                    \"description\": {\n                        \"type\": [\n                            \"string\",\n                            \"null\"\n                        ],\n                        \"minLength\": 1,\n                        \"maxLength\": 50\n                    }\n                },\n                \"required\": [\n                    \"name\"\n                ]\n            }\n        },\n        \"updatePathVariableRequests\": {\n            \"type\": \"array\",\n            \"items\": {\n                \"type\": \"object\",\n                \"properties\": {\n                    \"name\": {\n                        \"type\": \"string\",\n                        \"minLength\": 1,\n                        \"maxLength\": 20\n                    },\n                    \"description\": {\n                        \"type\": [\n                            \"string\",\n                            \"null\"\n                        ],\n                        \"minLength\": 1,\n                        \"maxLength\": 50\n                    }\n                },\n                \"required\": [\n                    \"name\"\n                ]\n            }\n        }\n    },\n    \"required\": [\n        \"method\",\n        \"name\",\n        \"endpoint\",\n        \"statusCode\"\n    ]\n}','',200,'2024-05-16 12:52:36','2024-05-17 13:14:13'),(11,19,'P27/A32/D19/A11','도메인 생성 API','도메인을 생성하는 API입니다.','/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}/domains','POST','{\n    \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n    \"title\": \"CreateDomainRequest\",\n    \"type\": \"object\",\n    \"properties\": {\n        \"name\": {\n            \"type\": \"string\",\n            \"minLength\": 1,\n            \"maxLength\": 20\n        }\n    },\n    \"required\": [\n        \"name\"\n    ],\n    \"additionalProperties\": false\n}','',200,'2024-05-16 12:53:49','2024-05-17 13:13:03'),(12,19,'P27/A32/D19/A12','도메인 명 수정','도메인 이름을 수정하는 API입니다.','/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}/domains/{domainId}','PATCH','{\n    \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n    \"type\": \"object\",\n    \"properties\": {\n        \"name\": {\n            \"type\": \"string\",\n            \"minLength\": 1,\n            \"maxLength\": 20\n        }\n    },\n    \"required\": [\n        \"name\"\n    ],\n    \"additionalProperties\": false\n}','',200,'2024-05-16 12:55:02','2024-05-17 13:13:50'),(13,19,'P27/A32/D19/A13','도메인 삭제','도메인 삭제를 위한 API입니다.','/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}/domains/{domainId}','DELETE','{\n    \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n    \"type\": \"object\",\n    \"properties\": {\n        \"name\": {\n            \"type\": \"string\",\n            \"minLength\": 1,\n            \"maxLength\": 20\n        }\n    },\n    \"required\": [\n        \"name\"\n    ],\n    \"additionalProperties\": false\n}','',200,'2024-05-16 12:56:04','2024-05-17 13:13:21'),(14,20,'P27/A32/D20/A14','파일 목록 조회','파일 목록 조회를 위한 API 입니다.','/api/v1/files?id=&type=','GET','','{\n    \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n    \"title\": \"GetFileInfoResponseList\",\n    \"type\": \"array\",\n    \"items\": {\n        \"type\": \"object\",\n        \"properties\": {\n            \"id\": {\n                \"type\": \"integer\",\n                \"description\": \"고유 식별자\"\n            },\n            \"address\": {\n                \"type\": \"string\",\n                \"description\": \"주소\"\n            },\n            \"referenceType\": {\n                \"type\": \"string\",\n                \"description\": \"참조 유형\"\n            },\n            \"referenceId\": {\n                \"type\": \"string\",\n                \"description\": \"참조 식별자\"\n            }\n        },\n        \"required\": [\n            \"id\",\n            \"address\",\n            \"referenceType\",\n            \"referenceId\"\n        ]\n    },\n    \"description\": \"GetFileInfoResponse 객체의 리스트\"\n}',200,'2024-05-16 13:17:11','2024-05-16 13:17:11'),(15,20,'P27/A32/D20/A15','파일 업로드','파일 업로드를 위한 API입니다.','/api/v1/files','POST','','{\n    \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n    \"title\": \"UploadFileResponseArray\",\n    \"type\": \"array\",\n    \"items\": {\n        \"type\": \"object\",\n        \"properties\": {\n            \"id\": {\n                \"type\": \"number\"\n            },\n            \"address\": {\n                \"type\": \"string\"\n            },\n            \"contentType\": {\n                \"type\": \"string\"\n            },\n            \"referenceId\": {\n                \"type\": \"string\"\n            },\n            \"referenceType\": {\n                \"type\": \"string\",\n                \"enum\": [\n                    \"PROFILE_IMAGE\",\n                    \"CHAT\",\n                    \"ISSUE\"\n                ]\n            }\n        },\n        \"required\": [\n            \"id\",\n            \"address\",\n            \"contentType\",\n            \"referenceId\",\n            \"referenceType\"\n        ]\n    }\n}',201,'2024-05-16 13:17:52','2024-05-16 13:17:52'),(16,19,'P27/A32/D19/A16','API 명세서 조회','API 명세서를 조회하는 api입니다.','/api/v1/projects/{projectId}/api-specifications/{apiSpecificationId}','GET','','{\n    \"$schema\": \"http://json-schema.org/draft-07/schema#\",\n    \"title\": \"GetApiSpecificationResponse\",\n    \"type\": \"object\",\n    \"properties\": {\n        \"apiSpecificationId\": {\n            \"type\": \"number\"\n        },\n        \"domains\": {\n            \"type\": \"array\",\n            \"items\": {\n                \"type\": \"object\",\n                \"properties\": {\n                    \"domainId\": {\n                        \"type\": \"number\"\n                    },\n                    \"name\": {\n                        \"type\": \"string\"\n                    },\n                    \"apis\": {\n                        \"type\": \"array\",\n                        \"items\": {\n                            \"type\": \"object\",\n                            \"properties\": {\n                                \"apiId\": {\n                                    \"type\": \"number\"\n                                },\n                                \"method\": {\n                                    \"type\": \"string\"\n                                },\n                                \"name\": {\n                                    \"type\": \"string\"\n                                },\n                                \"endpoint\": {\n                                    \"type\": \"string\"\n                                },\n                                \"statusCode\": {\n                                    \"type\": \"number\"\n                                }\n                            },\n                            \"required\": [\n                                \"apiId\",\n                                \"method\",\n                                \"name\",\n                                \"endpoint\",\n                                \"statusCode\"\n                            ]\n                        }\n                    }\n                },\n                \"required\": [\n                    \"domainId\",\n                    \"name\",\n                    \"apis\"\n                ]\n            }\n        }\n    },\n    \"required\": [\n        \"apiSpecificationId\",\n        \"domains\"\n    ]\n}',200,'2024-05-16 14:20:00','2024-05-16 14:21:05');
/*!40000 ALTER TABLE `api` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `api_specification`
--

DROP TABLE IF EXISTS `api_specification`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `api_specification` (
  `id` bigint NOT NULL,
  `path` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `api_specification_ibfk_1` FOREIGN KEY (`id`) REFERENCES `module` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `api_specification`
--

LOCK TABLES `api_specification` WRITE;
/*!40000 ALTER TABLE `api_specification` DISABLE KEYS */;
INSERT INTO `api_specification` VALUES (14,'P9/A14'),(18,'P11/A18'),(21,'P14/A21'),(23,'P22/A23'),(25,'P21/A25'),(26,'P24/A26'),(30,'P26/A30'),(32,'P27/A32'),(33,'P28/A33'),(34,'P31/A34'),(35,'P32/A35');
/*!40000 ALTER TABLE `api_specification` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `domain`
--

DROP TABLE IF EXISTS `domain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `domain` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `api_specification_id` bigint NOT NULL,
  `path` varchar(20) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `api_specification_id` (`api_specification_id`),
  CONSTRAINT `domain_ibfk_1` FOREIGN KEY (`api_specification_id`) REFERENCES `api_specification` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `domain`
--

LOCK TABLES `domain` WRITE;
/*!40000 ALTER TABLE `domain` DISABLE KEYS */;
INSERT INTO `domain` VALUES (10,21,'P14/A21/D10','Member','2024-05-13 21:34:16','2024-05-13 21:34:16'),(11,21,'P14/A21/D11','Board','2024-05-13 21:34:22','2024-05-13 21:34:22'),(12,21,'P14/A21/D12','Chat','2024-05-13 21:35:04','2024-05-13 21:35:04'),(15,23,'P22/A23/D15','ef','2024-05-14 16:21:52','2024-05-14 16:21:52'),(16,26,'P24/A26/D16','whatdoes','2024-05-14 16:42:38','2024-05-14 16:42:38'),(17,30,'P26/A30/D17','ddd','2024-05-16 09:41:38','2024-05-16 09:41:38'),(18,30,'P26/A30/D18','zzz','2024-05-16 09:41:42','2024-05-16 09:41:42'),(19,32,'P27/A32/D19','API 명세서','2024-05-16 12:50:05','2024-05-16 12:50:05'),(20,32,'P27/A32/D20','파일','2024-05-16 12:50:10','2024-05-16 12:50:10'),(21,33,'P28/A33/D21','goddamn unity','2024-05-16 13:29:29','2024-05-16 13:29:29'),(22,34,'P31/A34/D22','API','2024-05-17 13:05:29','2024-05-17 13:05:29'),(24,35,'P32/A35/D24','qwe','2024-05-19 01:00:55','2024-05-19 01:00:55');
/*!40000 ALTER TABLE `domain` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `file_information`
--

DROP TABLE IF EXISTS `file_information`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `file_information` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `original_name` varchar(50) NOT NULL,
  `path` varchar(1000) NOT NULL,
  `content_type` varchar(255) NOT NULL,
  `reference_id` varchar(255) NOT NULL,
  `reference_type` enum('PROFILE_IMAGE','CHAT','ISSUE') NOT NULL,
  `is_saved` tinyint(1) DEFAULT (0),
  `created_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=200 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `file_information`
--

LOCK TABLES `file_information` WRITE;
/*!40000 ALTER TABLE `file_information` DISABLE KEYS */;
INSERT INTO `file_information` VALUES (1,'test1.jpg','PROFILE_IMAGE/5f6122da-ecf3-4c70-852d-a75b338a024e.jpg','image/jpeg','1','PROFILE_IMAGE',0,'2024-05-08 05:43:27','2024-05-08 05:43:27'),(2,'talk.png','CHAT/b9d6aa6d-d997-4aff-88b6-858a1bcd4278.png','image/png','662ef127468e0c4fb83db31b','CHAT',0,'2024-05-08 05:43:48','2024-05-08 05:43:48'),(3,'talk.png','CHAT/431811b4-8a73-4130-abe0-b9b90a6b1508.png','image/png','662ef127468e0c4fb83db31b','CHAT',0,'2024-05-08 05:46:41','2024-05-08 05:46:41'),(4,'talk.png','CHAT/bcf2f668-a6ad-403f-b0d0-5a0f6a31335a.png','image/png','662ef127468e0c4fb83db31b','CHAT',0,'2024-05-08 05:46:54','2024-05-08 05:46:54'),(5,'talk.png','CHAT/9bdff354-582d-4b9a-aa63-e2863c674a05.png','image/png','662ef127468e0c4fb83db31b','CHAT',0,'2024-05-08 05:47:35','2024-05-08 05:47:35'),(6,'talk.png','CHAT/76df25ea-5b20-42dc-a0da-2286081b17b9.png','image/png','662ef127468e0c4fb83db31b','CHAT',0,'2024-05-08 05:48:51','2024-05-08 05:48:51'),(7,'talk.png','CHAT/756b8db2-c9cf-4d1c-8d95-160e2e1a4ea6.png','image/png','662ef127468e0c4fb83db31b','CHAT',0,'2024-05-08 05:48:56','2024-05-08 05:48:56'),(8,'talk.png','CHAT/1d08be50-3cf9-43ee-975e-0918a35f5e11.png','image/png','662ef127468e0c4fb83db31b','CHAT',0,'2024-05-08 05:49:13','2024-05-08 05:49:13'),(9,'talk.png','CHAT/7f4f29ee-ed08-4074-a507-6700f1e29ffa.png','image/png','662ef127468e0c4fb83db31b','CHAT',0,'2024-05-08 05:49:29','2024-05-08 05:49:29'),(10,'talk.png','CHAT/101b8d95-128b-4695-a36e-c821da23168e.png','image/png','662ef127468e0c4fb83db31b','CHAT',0,'2024-05-08 05:49:45','2024-05-08 05:49:45'),(11,'talk.png','CHAT/439c30e4-ff06-48dc-a1f0-008457e75e7a.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 05:56:53','2024-05-08 05:56:53'),(12,'Untitled.png','CHAT/0f2b9960-d930-41a7-a688-ffed1c70ff03.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 05:57:17','2024-05-08 05:57:17'),(13,'Untitled.png','CHAT/3f962b26-fc2d-4353-9a9c-04932b7cb11b.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 05:57:43','2024-05-08 05:57:43'),(14,'Untitled (1).png','CHAT/22261d84-bd49-4222-bf55-527564ccd500.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 05:57:43','2024-05-08 05:57:43'),(15,'Untitled.png','CHAT/440e4443-878b-49da-9e9b-73269cca41f3.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 05:58:46','2024-05-08 05:58:46'),(16,'new_extract(도형).zip','CHAT/991b2381-90c6-4be4-8edd-056d3850e17d.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:05:08','2024-05-08 06:05:08'),(17,'new_extract(도형).zip','CHAT/64a34c84-6fb8-4a73-aade-42a3ff2d7682.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:06:28','2024-05-08 06:06:28'),(18,'new_extract(도형).zip','CHAT/74bc392e-af48-4d1d-872e-5c8470dde0a6.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:06:50','2024-05-08 06:06:50'),(19,'new_extract(도형).zip','CHAT/07fcecb1-8076-4662-ad7e-b3c4fd792cca.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:07:09','2024-05-08 06:07:09'),(20,'new_extract(도형).zip','CHAT/38bb1ce9-c11f-4c52-bf13-71d87e0b193b.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:07:17','2024-05-08 06:07:17'),(21,'new_extract(도형).zip','CHAT/b479da37-f7ca-4a1c-8947-725a6cd91a22.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:07:32','2024-05-08 06:07:32'),(22,'new_extract(도형).zip','CHAT/224d0af7-74c0-4f7f-873a-fc46bf62bd2a.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:07:43','2024-05-08 06:07:43'),(23,'new_extract(도형).zip','CHAT/0cb47e0f-aded-4215-ab4e-4f79867e883f.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:07:58','2024-05-08 06:07:58'),(24,'new_extract(도형).zip','CHAT/b32728b2-b1e1-41cc-9dba-7ba1e33864fe.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:08:11','2024-05-08 06:08:11'),(25,'new_extract(도형).zip','CHAT/a2397c40-4ea3-4eb4-a5ef-b8340a1a261a.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:08:16','2024-05-08 06:08:16'),(26,'new_extract(도형).zip','CHAT/d5988247-b8ce-439d-9ddf-918026b6a451.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:08:22','2024-05-08 06:08:22'),(27,'new_extract(도형).zip','CHAT/c01212e5-168d-4984-ad64-0b95fdfdf76c.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:08:28','2024-05-08 06:08:28'),(28,'new_extract(도형).zip','CHAT/c28bb3c0-1a7d-4cf8-b04f-a9d18cca88d8.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:08:51','2024-05-08 06:08:51'),(29,'new_extract(도형).zip','CHAT/ef1dcfa5-138c-4f72-be65-99dd34a9ad43.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 06:13:36','2024-05-08 06:13:36'),(30,'test1.jpg','PROFILE_IMAGE/a6ba7e57-6d43-4e79-a3f7-1c9769abb044.jpg','image/jpeg','1','PROFILE_IMAGE',0,'2024-05-08 06:22:10','2024-05-08 06:22:10'),(31,'A504_Onterview.mp4','CHAT/c0a6b669-daea-490d-89cd-e5d4edf00762.mp4','video/mp4','asdoinweqofd','CHAT',0,'2024-05-08 07:03:56','2024-05-08 07:03:56'),(32,'A504_Onterview.mp4','CHAT/68f26438-3b54-42c4-891e-2a9377be7cc8.mp4','video/mp4','asdoinweqofd','CHAT',0,'2024-05-08 07:04:25','2024-05-08 07:04:25'),(33,'A504_Onterview.mp4','CHAT/457c1ff5-2e1b-4542-a44c-0a9b2f4c90d0.mp4','video/mp4','asdoinweqofd','CHAT',0,'2024-05-08 07:04:29','2024-05-08 07:04:29'),(34,'A504_Onterview.mp4','CHAT/d55de9fe-ca5a-4abd-a14a-a983fa1fbb74.mp4','video/mp4','asdoinweqofd','CHAT',0,'2024-05-08 07:05:32','2024-05-08 07:05:32'),(35,'A504_Onterview.mp4','CHAT/59ad237a-d042-4bf2-bb30-0c23519b52ee.mp4','video/mp4','asdoinweqofd','CHAT',0,'2024-05-08 07:05:42','2024-05-08 07:05:42'),(36,'A504_Onterview.mp4','CHAT/628a9082-8ec4-4441-9716-c5e1052a11af.mp4','video/mp4','asdoinweqofd','CHAT',0,'2024-05-08 07:06:32','2024-05-08 07:06:32'),(37,'A504_Onterview.mp4','CHAT/a578bdcf-cec3-46b4-b0bb-01dfebd3fa91.mp4','video/mp4','asdoinweqofd','CHAT',0,'2024-05-08 07:10:06','2024-05-08 07:10:06'),(38,'A504_Onterview.mp4','CHAT/42e86a97-925c-4292-b860-1dc339fe588e.mp4','video/mp4','asdoinweqofd','CHAT',0,'2024-05-08 07:11:23','2024-05-08 07:11:23'),(39,'Line.java','CHAT/70e0a64b-3a53-4a6f-a57a-13135cb85f62.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 07:17:22','2024-05-08 07:17:22'),(40,'Line.java','CHAT/dffc4ec2-13b6-4214-953d-3113b381b948.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 07:23:04','2024-05-08 07:23:04'),(41,'A504_Onterview.mp4','CHAT/2200338c-78e9-4ab2-9b13-be18f5454ce8.mp4','video/mp4','asdoinweqofd','CHAT',0,'2024-05-08 07:23:06','2024-05-08 07:23:06'),(42,'Line.java','CHAT/6779eac1-bef2-4030-acdb-736bb9171a0e.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 07:25:08','2024-05-08 07:25:08'),(43,'Untitled (1).png','CHAT/ff150acc-5a14-45aa-b7b2-635b6d001e6d.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:28:53','2024-05-08 07:28:53'),(44,'image.png','CHAT/15f42a93-7392-4a51-b7b2-75ee42c9ac4c.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:29:01','2024-05-08 07:29:01'),(45,'image.png','CHAT/67bbb03e-948c-414e-953e-9170c3ecbaff.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:29:25','2024-05-08 07:29:25'),(46,'image.png','CHAT/d4d5eabb-3054-4e76-ac44-8de027853422.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:29:47','2024-05-08 07:29:47'),(47,'image.png','CHAT/d87c21d9-59dd-44c6-b3e2-2705b0224f88.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:29:58','2024-05-08 07:29:58'),(48,'image.png','CHAT/d4fd54a9-c824-4844-b8ab-8e8a384b1b84.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:30:05','2024-05-08 07:30:05'),(49,'image.png','CHAT/1b28d33b-4bd6-4838-821b-89d69777c1cb.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:30:19','2024-05-08 07:30:19'),(50,'image.png','CHAT/460edab0-ab79-4c84-888c-1a9be6a9f58b.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:30:42','2024-05-08 07:30:42'),(51,'image.png','CHAT/d930d3f9-f8da-4671-8cb5-68f2d907d50a.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:30:47','2024-05-08 07:30:47'),(52,'image.png','CHAT/75f89518-d06c-458a-8c0c-312cdf0238c7.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:31:35','2024-05-08 07:31:35'),(53,'Untitled (1).png','CHAT/b7e36ec5-cae1-4580-a882-8dd1c274397a.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:31:40','2024-05-08 07:31:40'),(54,'Untitled (1).png','CHAT/16ed3209-349b-494e-b218-741dbd08e980.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:33:03','2024-05-08 07:33:03'),(55,'Untitled (1).png','CHAT/9d425a28-df20-4c37-9b39-a5a21b09d58d.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:33:30','2024-05-08 07:33:30'),(56,'Line.java','CHAT/50f7ca22-68b9-4e7d-bc8e-094835bfb459.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 07:33:45','2024-05-08 07:33:45'),(57,'Line.java','CHAT/a395daa7-85f9-4883-a2ff-ee62e66ce5c1.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 07:35:12','2024-05-08 07:35:12'),(58,'Line.java','CHAT/8de75cdc-dbc2-4335-b61a-f03ea977a795.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 07:35:34','2024-05-08 07:35:34'),(59,'Line.java','CHAT/de6939d7-89f1-455d-9b27-89b5efd27728.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 07:36:03','2024-05-08 07:36:03'),(60,'Line.java','CHAT/30a7028e-99f0-439b-9f35-6e98230a5516.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 07:36:09','2024-05-08 07:36:09'),(61,'Line.java','CHAT/9161cc03-8aee-4547-8619-4bec523802f6.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 07:36:31','2024-05-08 07:36:31'),(62,'Line.java','CHAT/f1900dab-74ae-4992-83f3-3cff32b1b57d.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 07:36:51','2024-05-08 07:36:51'),(63,'Line.java','CHAT/53a4a707-74ec-4142-804c-4acf74dc238d.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 07:37:49','2024-05-08 07:37:49'),(64,'Line.java','CHAT/2c049991-f940-49c1-87f8-d57dbcfa8262.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 07:38:33','2024-05-08 07:38:33'),(65,'Line.java','CHAT/dcc88138-b258-4897-a5cc-76a2779bcdb3.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 07:38:54','2024-05-08 07:38:54'),(66,'image.png','CHAT/4dce17dd-cb87-4ea5-b2b3-1b1d438c71bb.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:39:09','2024-05-08 07:39:09'),(67,'image.png','CHAT/49d0449d-c1df-4898-a5c0-def47a77da4f.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:39:52','2024-05-08 07:39:52'),(68,'Untitled.png','CHAT/5fb88633-9bc9-4b3f-a77c-879870973b7e.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:40:05','2024-05-08 07:40:05'),(69,'talk.png','CHAT/4761608e-26a5-415b-8ec4-894b4b369c64.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:40:26','2024-05-08 07:40:26'),(70,'image.png','CHAT/6c73bdbb-5f43-4ce3-83a0-eee71a184b78.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:43:32','2024-05-08 07:43:32'),(71,'Untitled (1).png','CHAT/432289c6-ccd5-4017-9741-52675c6154cc.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:43:46','2024-05-08 07:43:46'),(72,'image.png','CHAT/ea772412-3d20-482a-b975-b97bbe1bf2ac.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:44:22','2024-05-08 07:44:22'),(73,'image.png','CHAT/966dab66-77c4-42c9-a4af-551755e7a566.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:47:48','2024-05-08 07:47:48'),(74,'Untitled (1).png','CHAT/9b873568-fa9e-404c-a5cd-64919bb8876e.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:51:21','2024-05-08 07:51:21'),(75,'Untitled.png','CHAT/e7e5b732-4adf-4db2-b722-ceab46952c21.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:52:44','2024-05-08 07:52:44'),(76,'Untitled (1).png','CHAT/805f4a33-abe8-4755-b49e-024f90a79407.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:52:44','2024-05-08 07:52:44'),(77,'Untitled.png','CHAT/b0e67318-c66d-4ef5-a723-8376bf919adf.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:52:50','2024-05-08 07:52:50'),(78,'analyze2-2024-4-3.gif','CHAT/2655dbc5-d160-460a-96e8-cc9b8c54e59b.gif','image/gif','asdoinweqofd','CHAT',0,'2024-05-08 07:52:55','2024-05-08 07:52:55'),(79,'Untitled.png','CHAT/c12a74b1-334a-43de-aebd-c42526be308a.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:52:56','2024-05-08 07:52:56'),(80,'image.png','CHAT/aa7a1556-740a-45db-a276-21dfe079513b.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:53:02','2024-05-08 07:53:02'),(81,'image.png','CHAT/2334daf1-0ad1-4f59-8439-a8d25f73c2bc.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:54:54','2024-05-08 07:54:54'),(82,'image.png','CHAT/9f39cbbd-1f0c-41b1-8d3b-9556c8b67511.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:56:41','2024-05-08 07:56:41'),(83,'image.png','CHAT/e1f41ac1-da2b-4712-964e-32219a66fda4.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:56:45','2024-05-08 07:56:45'),(84,'image.png','CHAT/514e21e1-5008-46c5-9291-c4f90f7262e2.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:56:49','2024-05-08 07:56:49'),(85,'image.png','CHAT/799ae7d2-07dd-4669-bec2-c6cccf27ee36.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:57:12','2024-05-08 07:57:12'),(86,'image.png','CHAT/813f3aae-b629-4d18-a7a0-bfc433eeeef7.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:57:15','2024-05-08 07:57:15'),(87,'image.png','CHAT/e5c18edb-f685-49b9-86d9-cb93aa5e68c8.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 07:58:03','2024-05-08 07:58:03'),(88,'image.png','CHAT/08c50115-8f51-4024-973c-5b9118f11c05.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 08:10:56','2024-05-08 08:10:56'),(89,'Line.java','CHAT/0ecb9587-a367-4299-ba97-79093278c85f.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 08:10:59','2024-05-08 08:10:59'),(90,'Untitled.png','CHAT/a375aa6e-166d-4e60-b6c5-4d9ad447ad0a.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 08:11:04','2024-05-08 08:11:04'),(91,'Untitled (1).png','CHAT/b348d67b-8f86-4fdb-a509-f3d889fa53ca.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 08:11:04','2024-05-08 08:11:04'),(92,'main-2024-4-3.gif','CHAT/635756f1-b1d8-4be6-8272-a3f1170a618c.gif','image/gif','asdoinweqofd','CHAT',0,'2024-05-08 08:11:07','2024-05-08 08:11:07'),(93,'Line.java','CHAT/9caffa3c-9fd1-410e-936a-fb5ce7689b4a.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 08:11:08','2024-05-08 08:11:08'),(94,'Untitled.png','CHAT/6a1dd6e7-aad4-42b3-868e-b85e64139b77.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 08:11:08','2024-05-08 08:11:08'),(95,'Untitled (1).png','CHAT/2f1052a2-3318-4053-a92c-868ff255591c.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 08:11:08','2024-05-08 08:11:08'),(96,'Untitled.png','CHAT/4439634e-f3cc-4edc-935a-f53599f36442.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 08:11:09','2024-05-08 08:11:09'),(97,'Untitled (1).png','CHAT/69aa4ca1-fc74-441a-94f4-a000b19da7df.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 08:11:09','2024-05-08 08:11:09'),(98,'Untitled.png','CHAT/9dfd99d8-56b3-45a6-a00d-f8d372226811.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 08:11:11','2024-05-08 08:11:11'),(99,'main-2024-4-3.gif','CHAT/d05f6a2c-5546-4bef-82ed-010ee0790b83.gif','image/gif','asdoinweqofd','CHAT',0,'2024-05-08 08:11:14','2024-05-08 08:11:14'),(100,'main-2024-4-3.gif','CHAT/a66d008b-a856-4212-a4f2-0779d54cbe81.gif','image/gif','asdoinweqofd','CHAT',0,'2024-05-08 08:11:17','2024-05-08 08:11:17'),(101,'main-2024-4-3.gif','CHAT/e7ab930f-07c2-46f9-bcd7-0804dbd6855d.gif','image/gif','asdoinweqofd','CHAT',0,'2024-05-08 08:11:19','2024-05-08 08:11:19'),(102,'main-2024-4-3.gif','CHAT/fdebc3f4-aa36-4481-8e89-21ac8fc109b0.gif','image/gif','asdoinweqofd','CHAT',0,'2024-05-08 08:11:21','2024-05-08 08:11:21'),(103,'Untitled.png','CHAT/2b2cfc30-4705-41aa-a9e4-501e20f9cb5f.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 08:13:17','2024-05-08 08:13:17'),(104,'Untitled (1).png','CHAT/baa5a876-f275-4759-afc9-f992c9d658dd.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 08:13:17','2024-05-08 08:13:17'),(105,'main-2024-4-3.gif','CHAT/95ec01eb-d36f-4d43-a184-cccdae3866d0.gif','image/gif','asdoinweqofd','CHAT',0,'2024-05-08 08:13:20','2024-05-08 08:13:20'),(106,'analyze2-2024-4-3.gif','CHAT/3e668b55-26b8-41fa-8e75-99001d7d5031.gif','image/gif','asdoinweqofd','CHAT',0,'2024-05-08 08:13:31','2024-05-08 08:13:31'),(107,'[오브젠] 면접확인서_최재용.pdf','CHAT/c3188ced-0e88-49c7-ab0c-cdaa69a8f863.pdf','application/pdf','asdoinweqofd','CHAT',0,'2024-05-08 08:14:55','2024-05-08 08:14:55'),(108,'최재용_페스카로_발표자료.pdf','CHAT/25755402-5865-422f-9b5d-29f797d66b8b.pdf','application/pdf','asdoinweqofd','CHAT',0,'2024-05-08 08:14:55','2024-05-08 08:14:55'),(109,'20240304_출결확인서_최재용[서울_7반].pdf','CHAT/ba12b827-0f5a-4cb2-bc56-f6850b37dbf8.pdf','application/pdf','asdoinweqofd','CHAT',0,'2024-05-08 08:14:56','2024-05-08 08:14:56'),(110,'58.zip','CHAT/245dbc71-7fe4-44c6-a5da-c6be1c41f845.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 08:14:57','2024-05-08 08:14:57'),(111,'4511796.zip','CHAT/633da186-af38-4e70-b6ff-81c216a744dd.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 08:15:02','2024-05-08 08:15:02'),(112,'Untitled.png','CHAT/8c18cbf7-780b-43f8-bf1f-bad114cfccc0.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 08:15:14','2024-05-08 08:15:14'),(113,'image.png','CHAT/b6dc498c-1f20-4e0e-8a1c-cf03fae2cf47.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 08:16:08','2024-05-08 08:16:08'),(114,'new_extract(도형).zip','CHAT/6aea1dea-b743-408a-9e92-aad748d68628.zip','application/x-zip-compressed','asdoinweqofd','CHAT',0,'2024-05-08 08:16:19','2024-05-08 08:16:19'),(115,'Line.java','CHAT/45129c67-daf2-4879-bb16-ced0d8b47e4a.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 08:17:04','2024-05-08 08:17:04'),(116,'Line.java','CHAT/46999c60-cfb3-49b4-a371-a1e42f2e95de.java','application/octet-stream','asdoinweqofd','CHAT',0,'2024-05-08 08:19:38','2024-05-08 08:19:38'),(117,'image.png','CHAT/43f50cc7-92b9-4574-a592-a9b34e2ffd0d.png','image/png','asdoinweqofd','CHAT',0,'2024-05-08 08:26:49','2024-05-08 08:26:49'),(118,'image.png','CHAT/705fe3a1-42bf-4b46-a407-8f0b70165c9c.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 05:09:41','2024-05-09 05:09:41'),(119,'image.png','CHAT/430a3caf-4447-4b7e-b932-310d476bd189.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 05:12:18','2024-05-09 05:12:18'),(120,'image.png','CHAT/abb142bb-388b-4881-b8a6-c0f54e4785a7.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 05:13:10','2024-05-09 05:13:10'),(121,'image.png','CHAT/fc4348cb-a947-4614-bc23-2c5bae606f02.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 05:13:52','2024-05-09 05:13:52'),(122,'image.png','CHAT/003f5161-2e65-44e5-8af4-0bb662d45e8a.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 05:15:17','2024-05-09 05:15:17'),(123,'image.png','CHAT/c5c2a641-cfd4-4ad0-bf76-f8c095594eb7.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 05:18:48','2024-05-09 05:18:48'),(124,'image.png','CHAT/cc62b0be-29fc-48d6-a704-08e4a8f251e2.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 05:20:05','2024-05-09 05:20:05'),(125,'image.png','CHAT/5be57654-0ded-447e-a001-a34f8c212644.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 05:22:49','2024-05-09 05:22:49'),(126,'talk.png','CHAT/ed46607c-f197-4e2e-8119-a0e75f8f50f5.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 05:24:19','2024-05-09 05:24:19'),(127,'image.png','CHAT/ce118cba-2f03-4c3b-b915-20df8d8c7af1.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 05:25:38','2024-05-09 05:25:38'),(130,'image.png','CHAT/d1e3a66a-089e-4e00-922a-6d475303e5b5.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 05:29:11','2024-05-09 05:29:11'),(132,'image.png','CHAT/5ca99ec1-d0f9-45a7-bd0d-35e09dab9258.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 06:15:09','2024-05-09 06:15:09'),(133,'image.png','CHAT/efc1a01d-acc5-48dc-88fa-ab19ed3d99d9.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 06:15:46','2024-05-09 06:15:46'),(134,'image.png','CHAT/145f44cb-4522-436b-b921-5942bb7b8b15.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 06:20:01','2024-05-09 06:20:01'),(135,'image.png','CHAT/aa067f09-01c1-41e1-bf85-80495ffc186f.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 06:45:08','2024-05-09 06:45:08'),(136,'Untitled.png','CHAT/8bace56d-7dae-46f8-9486-0f26231021a5.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 06:45:54','2024-05-09 06:45:54'),(137,'Untitled (1).png','CHAT/53b29062-2a43-4b9b-943f-8aaae66d465a.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 06:45:54','2024-05-09 06:45:54'),(138,'main-2024-4-3.gif','CHAT/6a8ff129-972b-49d0-b561-3c84fe6172e7.gif','image/gif','asdoinweqofd','CHAT',0,'2024-05-09 06:45:59','2024-05-09 06:45:59'),(139,'analyze2-2024-4-3.gif','CHAT/2bb0812e-bcda-4cf9-af4e-336e3cb71672.gif','image/gif','asdoinweqofd','CHAT',0,'2024-05-09 06:46:08','2024-05-09 06:46:08'),(140,'Untitled.png','CHAT/b46e9aa0-ecd8-4485-a8d3-88112590b3a6.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 06:47:17','2024-05-09 06:47:17'),(141,'Untitled (1).png','CHAT/2ebcd0d3-3a7c-4ab9-aafe-54d2fad22c46.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 06:47:17','2024-05-09 06:47:17'),(142,'main-2024-4-3.gif','CHAT/f5157127-5353-49f4-b9d1-64be763412ba.gif','image/gif','asdoinweqofd','CHAT',0,'2024-05-09 06:47:25','2024-05-09 06:47:25'),(143,'analyze2-2024-4-3.gif','CHAT/a107f3c3-c2af-4a3b-a55c-1068eda26c8d.gif','image/gif','asdoinweqofd','CHAT',0,'2024-05-09 06:47:31','2024-05-09 06:47:31'),(145,'Untitled.png','CHAT/132e71e5-5be6-4b3e-8bb3-0bf9ab562cfc.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 06:54:01','2024-05-09 06:54:01'),(146,'Untitled (1).png','CHAT/bb089e93-40e8-4e10-921f-74396439b7fc.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 06:54:01','2024-05-09 06:54:01'),(148,'main-2024-4-3.gif','CHAT/c0643e34-a539-4dff-8201-d7416af5f484.gif','image/gif','asdoinweqofd','CHAT',0,'2024-05-09 06:54:14','2024-05-09 06:54:14'),(150,'image.png','CHAT/cfa6644d-bdd1-48cd-912f-06c8503fa418.png','image/png','asdoinweqofd','CHAT',0,'2024-05-09 07:23:47','2024-05-09 07:23:47'),(151,'소명해당일_출결변경요청서_이름[지역_반].docx','CHAT/63b2233c-5056-4c02-9ade-6209b7980e51.docx','application/vnd.openxmlformats-officedocument.wordprocessingml.document','asdoinweqofd','CHAT',0,'2024-05-09 07:25:19','2024-05-09 07:25:19'),(154,'talk (2).png','CHAT/57531b4f-d244-47e4-9d7b-dd49212b22f3.png','image/png','asdoinweqofd','CHAT',0,'2024-05-13 02:48:57','2024-05-13 02:48:57'),(155,'256.png','CHAT/4f4e1474-089c-4e84-8b75-b7e15d250fa3.png','image/png','asdoinweqofd','CHAT',0,'2024-05-13 03:39:06','2024-05-13 03:39:06'),(156,'talk.png','CHAT/4e132d16-f2b4-4cd8-98ba-c36eea228544.png','image/png','asdoinweqofd','CHAT',0,'2024-05-13 03:41:17','2024-05-13 03:41:17'),(157,'Architecture.png','CHAT/6d30446b-e958-4960-af8f-82b721192d62.png','image/png','asdoinweqofd','CHAT',0,'2024-05-13 03:41:24','2024-05-13 03:41:24'),(158,'talk (1).png','CHAT/b60cde1f-abdf-4f68-9221-d983a55caaff.png','image/png','asdoinweqofd','CHAT',0,'2024-05-13 03:41:28','2024-05-13 03:41:28'),(159,'image.png','CHAT/0a9794bb-b0fe-46cb-a7ef-fe8b7c2cc7e7.png','image/png','6642de571894b7166bedee91','CHAT',0,'2024-05-14 03:53:07','2024-05-14 03:53:07'),(160,'mingw-w64-v11.0.0.zip','CHAT/6e80da58-39df-425b-9d4e-69eece0c7e5c.zip','application/x-zip-compressed','6642de571894b7166bedee91','CHAT',0,'2024-05-14 04:04:02','2024-05-14 04:04:02'),(162,'Architecture.png','CHAT/ee1c0744-b578-4f80-9489-637b21e2a935.png','image/png','6642fb2e1894b7166bedef3b','CHAT',0,'2024-05-14 05:49:01','2024-05-14 05:49:01'),(163,'image.png','CHAT/dfa9840a-c2e6-4d59-acbc-a727dfbc21d6.png','image/png','6641702435a19829fb51144f','CHAT',0,'2024-05-14 06:44:16','2024-05-14 06:44:16'),(164,'image.png','CHAT/0a38237f-cfdd-4766-a33e-879da8595138.png','image/png','6642de571894b7166bedee91','CHAT',0,'2024-05-14 07:21:13','2024-05-14 07:21:13'),(165,'mingw-w64-v11.0.0.zip','CHAT/3fb6fc3c-0b0b-4027-9886-027635aab119.zip','application/x-zip-compressed','6642de571894b7166bedee91','CHAT',0,'2024-05-14 07:21:26','2024-05-14 07:21:26'),(166,'spring-web-6.1.5-sources.jar','CHAT/956a36ff-9980-4542-99af-4261a335afb2.jar','application/octet-stream','6642ed6c1894b7166bedeea5','CHAT',0,'2024-05-14 08:06:46','2024-05-14 08:06:46'),(167,'image.png','CHAT/a174b74a-3a48-495c-952c-e478c0457ac0.png','image/png','6642ed6c1894b7166bedeea5','CHAT',0,'2024-05-14 08:06:59','2024-05-14 08:06:59'),(168,'image.png','CHAT/56d67997-72cb-4b2c-a4ef-94888ddade91.png','image/png','6641702435a19829fb51144f','CHAT',0,'2024-05-14 08:21:33','2024-05-14 08:21:33'),(169,'spring-web-6.1.5-sources.jar','CHAT/dcf99196-d0bc-4a91-bab7-de8a0a010adb.jar','application/octet-stream','6641702435a19829fb51144f','CHAT',0,'2024-05-14 08:21:39','2024-05-14 08:21:39'),(170,'mingw-w64-v11.0.0.zip','CHAT/210e2656-926a-4dc9-b53e-5824a1efc3a0.zip','application/x-zip-compressed','6642de571894b7166bedee91','CHAT',0,'2024-05-14 08:21:58','2024-05-14 08:21:58'),(171,'image.png','CHAT/d45023b9-989b-4231-a6fc-bb783cebbf4e.png','image/png','6642fb2e1894b7166bedef3b','CHAT',0,'2024-05-14 08:25:53','2024-05-14 08:25:53'),(172,'image.png','CHAT/fde0b84d-7630-4027-a80f-d6d9df549be2.png','image/png','6642fb2e1894b7166bedef3b','CHAT',0,'2024-05-14 08:26:03','2024-05-14 08:26:03'),(173,'image.png','CHAT/6278147d-4de1-42d1-925e-1952a4661686.png','image/png','6641702435a19829fb51144f','CHAT',0,'2024-05-14 08:26:29','2024-05-14 08:26:29'),(174,'mingw-w64-v11.0.0.zip','CHAT/07cfbfbd-c923-49c4-88e1-cc39aa7f5a17.zip','application/x-zip-compressed','664320ba6b9a265cd1f6a390','CHAT',0,'2024-05-14 08:28:51','2024-05-14 08:28:51'),(175,'mingw-w64-v11.0.0.zip','CHAT/adfc1368-8a2e-4c17-9371-eeac51c844f1.zip','application/x-zip-compressed','664320ba6b9a265cd1f6a390','CHAT',0,'2024-05-14 08:30:25','2024-05-14 08:30:25'),(180,'A504_Onterview.mp4','CHAT/093bea9c-6c67-49ee-9f25-77a6847d43af.mp4','video/mp4','664320ba6b9a265cd1f6a390','CHAT',0,'2024-05-14 08:36:34','2024-05-14 08:36:34'),(182,'4511796.zip','CHAT/de577447-b0da-4e91-acaf-146fc3e7fad8.zip','application/x-zip-compressed','664320ba6b9a265cd1f6a390','CHAT',0,'2024-05-16 03:39:25','2024-05-16 03:39:25'),(183,'그림1.png','CHAT/7f1c4e2c-cda7-4444-9fee-a302ac3f9591.png','image/png','66458a8f72624630f4a8199f','CHAT',0,'2024-05-16 04:26:09','2024-05-16 04:26:09'),(188,'Untitled.png','CHAT/f8dbc69a-dc21-4a4e-9c38-9f1ec202d2cc.png','image/png','66458d3872624630f4a81a28','CHAT',0,'2024-05-16 04:45:06','2024-05-16 04:45:06'),(194,'Untitled.png','CHAT/d53a6599-2d0e-47c2-b1bd-8141eb6bfd57.png','image/png','664590fd72624630f4a81a66','CHAT',0,'2024-05-16 04:55:48','2024-05-16 04:55:48'),(195,'★(10기 2학기) 자율 프로젝트 결과물 활용 동의서_서울_A301_최재용.docx','CHAT/a9fc5144-0c6b-4bad-b87a-0e836b7ef48f.docx','application/vnd.openxmlformats-officedocument.wordprocessingml.document','664590fd72624630f4a81a66','CHAT',0,'2024-05-16 04:56:47','2024-05-16 04:56:47'),(196,'채팅_최재용.webm','CHAT/c659c504-3ee8-44fb-a487-4cc60f0dbdca.webm','video/webm','664590fd72624630f4a81a66','CHAT',0,'2024-05-16 04:58:16','2024-05-16 04:58:16'),(197,'ssafy_image.png','PROFILE_IMAGE/1e4730d5-368f-41eb-94aa-bed4ed6afc49.png','image/png','ssafy-image','PROFILE_IMAGE',0,'2024-05-16 05:22:22','2024-05-16 05:22:22'),(198,'비숍 테케.txt','CHAT/055a198d-57fc-48cd-92ef-2d6c0ee84339.txt','text/plain','6646da4a72624630f4a81a7a','CHAT',0,'2024-05-17 04:18:04','2024-05-17 04:18:04'),(199,'IMG_0208.jpeg','CHAT/e592ada5-b573-495b-8798-8d5dc42d884a.jpeg','image/jpeg','6646da4a72624630f4a81a7a','CHAT',0,'2024-05-17 04:19:08','2024-05-17 04:19:08');
/*!40000 ALTER TABLE `file_information` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `issue`
--

DROP TABLE IF EXISTS `issue`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `issue` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `title` varchar(90) NOT NULL,
  `content` text,
  `importance` tinyint NOT NULL DEFAULT '0',
  `status` tinyint NOT NULL DEFAULT '0',
  `kanbanboard_id` bigint NOT NULL,
  `charger_id` bigint DEFAULT NULL,
  `label_id` bigint DEFAULT NULL,
  `path` varchar(20) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_ISSUES_KANBANBOARD_ID` (`kanbanboard_id`),
  KEY `FK_ISSUES_MEMBER_ID` (`charger_id`),
  KEY `FK_ISSUES_LABEL_ID` (`label_id`),
  CONSTRAINT `FK_ISSUES_KANBANBOARD_ID` FOREIGN KEY (`kanbanboard_id`) REFERENCES `kanbanboard` (`id`) ON DELETE CASCADE,
  CONSTRAINT `FK_ISSUES_LABEL_ID` FOREIGN KEY (`label_id`) REFERENCES `label` (`id`),
  CONSTRAINT `FK_ISSUES_MEMBER_ID` FOREIGN KEY (`charger_id`) REFERENCES `member` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=25 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `issue`
--

LOCK TABLES `issue` WRITE;
/*!40000 ALTER TABLE `issue` DISABLE KEYS */;
INSERT INTO `issue` VALUES (1,'dd','',2,2,16,NULL,NULL,'P10/K16/I1','2024-05-10 14:12:57','2024-05-10 14:12:57'),(2,'ddd','ddd',1,1,16,NULL,NULL,'P10/K16/I2','2024-05-12 17:06:59','2024-05-12 17:06:59'),(3,'ERD 설계','',1,2,20,NULL,1,'P14/K20/I3','2024-05-14 09:27:42','2024-05-14 09:29:51'),(4,'피그마 설계','',1,3,20,NULL,1,'P14/K20/I4','2024-05-14 09:28:40','2024-05-14 09:29:46'),(5,'Spring 프로젝트 구조 설계','',3,2,20,NULL,2,'P14/K20/I5','2024-05-14 09:32:09','2024-05-14 09:39:30'),(6,'React 프로젝트 구조 설계','',3,2,20,NULL,3,'P14/K20/I6','2024-05-14 09:32:29','2024-05-14 09:39:34'),(7,'엔터티 작성','',2,1,20,NULL,2,'P14/K20/I7','2024-05-14 09:38:46','2024-05-14 09:38:46'),(8,'로그인 페이지 구현','',3,1,20,NULL,3,'P14/K20/I8','2024-05-14 09:39:25','2024-05-14 09:39:25'),(9,'회원가입 API 구현','# 회원가입\n\n### 필수값\n- 이름\n- 생년월일\n- 닉네임\n- 이메일\n- 비밀번호\n### 선택 값\n- 추천인',3,1,20,1,2,'P14/K20/I9','2024-05-14 09:43:42','2024-05-14 13:31:08'),(10,'sdfsdf','sdfsfsdf',3,1,24,124,5,'P21/K24/I10','2024-05-14 14:47:29','2024-05-14 14:47:38'),(11,'fsadfsdf','sfsadfds',3,1,27,126,NULL,'P24/K27/I11','2024-05-14 16:43:22','2024-05-14 16:43:22'),(13,'Api 명세서 작성','',3,3,31,NULL,9,'P27/K31/I13','2024-05-16 12:41:29','2024-05-16 12:42:20'),(14,'요구사항 정의서 작성','',3,3,31,NULL,NULL,'P27/K31/I14','2024-05-16 12:41:43','2024-05-16 12:41:43'),(15,'기능 명세서 작성','',3,3,31,NULL,NULL,'P27/K31/I15','2024-05-16 12:41:53','2024-05-16 12:41:53'),(16,'ERD 설계','',3,2,31,142,13,'P27/K31/I16','2024-05-16 12:42:08','2024-05-16 12:53:01'),(17,'피그마 화면 설계','',3,2,31,142,13,'P27/K31/I17','2024-05-16 12:42:37','2024-05-16 12:53:05'),(18,'Spring 초기 셋팅','# Version\n- Java : 21\n-  Spring boot : 3.2.4\n\n## Dependency\n- jpa\n- querydsl\n- spring boot actuator\n- json schema validator\n- json parse\n- minio\n- stomp ',3,2,31,142,9,'P27/K31/I18','2024-05-16 12:43:02','2024-05-16 12:50:46'),(24,'엔터티 작성','# 멤버\n- 아이디\n- 비밀번호\n\n# 보드\n- 제목',2,2,31,NULL,9,'P27/K31/I24','2024-05-16 16:25:49','2024-05-16 16:25:56');
/*!40000 ALTER TABLE `issue` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kanbanboard`
--

DROP TABLE IF EXISTS `kanbanboard`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kanbanboard` (
  `id` bigint NOT NULL,
  `path` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `FK_KANBANBOARD_MODULE_ID` FOREIGN KEY (`id`) REFERENCES `module` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kanbanboard`
--

LOCK TABLES `kanbanboard` WRITE;
/*!40000 ALTER TABLE `kanbanboard` DISABLE KEYS */;
INSERT INTO `kanbanboard` VALUES (15,'P9/K15'),(16,'P10/K16'),(17,'P11/K17'),(20,'P14/K20'),(24,'P21/K24'),(27,'P24/K27'),(28,'P25/K28'),(29,'P26/K29'),(31,'P27/K31');
/*!40000 ALTER TABLE `kanbanboard` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `label`
--

DROP TABLE IF EXISTS `label`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `label` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(50) NOT NULL,
  `kanbanboard_id` bigint NOT NULL,
  `path` varchar(20) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_LABEL_KANBANBOARD_ID` (`kanbanboard_id`),
  CONSTRAINT `FK_LABEL_KANBANBOARD_ID` FOREIGN KEY (`kanbanboard_id`) REFERENCES `kanbanboard` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `label`
--

LOCK TABLES `label` WRITE;
/*!40000 ALTER TABLE `label` DISABLE KEYS */;
INSERT INTO `label` VALUES (1,'공통',20,'P14/K20/L1','2024-05-14 09:28:47','2024-05-14 09:28:47'),(2,'BackEnd',20,'P14/K20/L2','2024-05-14 09:28:57','2024-05-14 09:28:57'),(3,'FrontEnd',20,'P14/K20/L3','2024-05-14 09:29:06','2024-05-14 09:29:06'),(5,'sdfsdf',24,'P21/K24/L5','2024-05-14 14:47:16','2024-05-14 14:47:16'),(6,'ss',29,'P26/K29/L6','2024-05-16 09:40:28','2024-05-16 09:40:28'),(7,'zz',29,'P26/K29/L7','2024-05-16 09:40:35','2024-05-16 09:40:35'),(8,'zzzzzzzzzzzzzzzzzzz',29,'P26/K29/L8','2024-05-16 09:40:39','2024-05-16 09:40:39'),(9,'BackEnd',31,'P27/K31/L9','2024-05-16 12:40:32','2024-05-16 12:40:32'),(10,'FrontEnd',31,'P27/K31/L10','2024-05-16 12:40:37','2024-05-16 12:40:37'),(11,'Data',31,'P27/K31/L11','2024-05-16 12:40:41','2024-05-16 12:40:41'),(12,'기획',31,'P27/K31/L12','2024-05-16 12:40:48','2024-05-16 12:40:48'),(13,'공통',31,'P27/K31/L13','2024-05-16 12:40:54','2024-05-16 12:40:54'),(14,'서류',31,'P27/K31/L14','2024-05-16 14:34:58','2024-05-16 14:34:58'),(15,'라벨',31,'P27/K31/L15','2024-05-16 16:23:40','2024-05-16 16:23:40'),(16,'라벨 1',31,'P27/K31/L16','2024-05-16 16:26:13','2024-05-16 16:26:13');
/*!40000 ALTER TABLE `label` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `member`
--

DROP TABLE IF EXISTS `member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `member` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `nickname` varchar(30) NOT NULL,
  `email` varchar(50) NOT NULL,
  `password` varchar(100) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `profile` varchar(1000) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`),
  UNIQUE KEY `nickname_unique` (`nickname`),
  UNIQUE KEY `email_unique` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=147 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `member`
--

LOCK TABLES `member` WRITE;
/*!40000 ALTER TABLE `member` DISABLE KEYS */;
INSERT INTO `member` VALUES (1,'박상현','psnew14@naver.com','$2a$10$jW9iiMORUci03V91pGgqs.ONN05sGH2IdTRjbrzAxEG2ERQiAhvGy','2024-04-30 10:48:42','2024-04-30 10:48:42',''),(121,'테스트','psnew14@aaa.com','$2a$10$QbSbzo0JH1EUNF1kO4QbC.IMa71nUcwsmh4CSICP3MCA.2EiV21ry','2024-04-30 15:44:30','2024-04-30 15:44:30',''),(124,'박상현1','psnew14@gmail.com','$2a$10$nOy9CFHaPeHzE8h70fpb5OdVnIS.bHALDB7xsiDEpws8b79j9plpO','2024-05-02 12:55:47','2024-05-02 12:55:47',''),(125,'슈밤','wjs6265@gmail.com','$2a$10$YD7AEkCdd86D9lyO10p1zO6/tQeclbcVi7uzyoxIupNtJSp0OFe5G','2024-05-02 16:30:57','2024-05-02 16:30:57',''),(126,'응애에요','test@test.com','$2a$10$H3uxBDSrqB1boz0TKQVF1.MMhLJnCYgl/ymrGNu68Anb4a86y0mwy','2024-05-03 10:47:00','2024-05-03 10:47:00',''),(127,'김병주','ilgooinkorea@gmail.com','$2a$10$TpQulR2Rumvv3z0tIweQROLGvhew4ZJdDA7dSAi9GD0uLAdZZACju','2024-05-03 11:00:19','2024-05-03 02:05:36',''),(128,'1','1@1.com','$2a$10$YQmo3cS.xiag3mAbxteY6uoGu.RTVbXavNLSFObQKPyOCq5sWSTq6','2024-05-07 17:30:15','2024-05-07 17:30:15',''),(129,'rhrnak','rhrnak608@gmail.com','$2a$10$0LYPuS6/YEhSlNc4pwbxCu1QDoqwcR3NPO5719n3lSLUs7kI30aV2','2024-05-08 10:48:12','2024-05-08 10:48:12',''),(130,'최재용[서울_3반_A301]','chlwodyd9829@naver.com','$2a$10$b8yu.i6tkDjgDUZCu5zAH.P3MTk189cANlJHUg.yfu6m5TPQVPMLi','2024-05-08 12:32:37','2024-05-08 12:32:37',''),(131,'nickname','email@naver.com','$2a$10$WJHYJZ6XKExlp4Gj9MWFAO.akvaXfbJ97yBLYzhUgSWSjWkuIbwR6','2024-05-08 14:32:53','2024-05-08 14:32:53',''),(132,'슈밤바','wjs1111@gmail.com','$2a$10$48hbbMiWOa0.tBDQeRXyCOjnndh6H8oS8CyRkMnR0SIrvu86Y6lzu','2024-05-10 10:39:39','2024-05-10 10:39:39',''),(133,'tbqka','wjs2222@gmail.com','$2a$10$IzMh6VU2/MxDV.bz4/7NAeBZU/wEz0Kx9VT4Rw12RR5AzJ6RQFhuq','2024-05-10 13:14:21','2024-05-10 13:14:21',''),(134,'킹갓구','iloveilgooinkorea@gmail.com','$2a$10$oEDZXMBV5hGp6ViTee8WxehzO7UX7ne55DNwd./6TBjuNuG7wXOLy','2024-05-14 10:45:11','2024-05-14 10:45:11',''),(135,'pass123','test11@naver.com','$2a$10$kfKAtUI9yoPDYUYfsSVk7eFltwRUrCOq2U.0Xnlt/lYDWJN793i0C','2024-05-14 12:44:28','2024-05-14 12:44:28',''),(136,'아메리카노','wocks2@naver.com','$2a$10$Lq414a4YMwjrkLB.Bzc0ZOg9Aey4ZbyxKgzd2riyBE32Ium.E530y','2024-05-14 13:46:56','2024-05-14 13:46:56',''),(137,'tester','test1@test.com','$2a$10$4E1XNMz5aGIfGcJyy72Cjeinqte9V3Emx9BZUHeJPxT8qh7qJpQRe','2024-05-14 16:40:38','2024-05-14 16:40:38',''),(138,'12345678','ssafy1@ssafy.com','$2a$10$ikiR63ZGojzqMqE1/Uvq0eKi5ImP/D7y5cpq1khx2Krm.3ev1dV66','2024-05-16 11:54:11','2024-05-16 11:54:11',''),(139,'SSAFY_CONSULTANT','ssafy@ssafy.com','$2a$10$5kEfLfUSJm8JVfLfYeONmuuhzMz/RJhU8mHDP/WVTpP4dzyPteI4e','2024-05-16 11:54:32','2024-05-16 11:54:32',''),(140,'SSAFY_COACH','ssafya@ssafy.com','$2a$10$2LOFRFdnYTBD6Xo8JhxxyupYfP0VxCJlVNMy7iU6vqUw3z7bnqA9q','2024-05-16 11:54:45','2024-05-16 11:54:45',''),(141,'전승열','wjs1234@gmail.com','$2a$10$r00QEFkyOAHjybp06ZsU2.Qzz8/NPylLfXOZvNblhm1ERF86qECPe','2024-05-16 12:38:16','2024-05-16 12:38:16',''),(142,'여일구','doongdang@test.com','$2a$10$1CGG80V3NuYBCsYEry0oMOJMgoygBh9zbNmQMvbcCeWW5W6Nz2L8a','2024-05-16 12:38:18','2024-05-16 12:38:18',''),(143,'킹갓병주현일구','qkrdustj45@naver.com','$2a$10$JOMMQuHJqMDrz7Dzgxt0tuvdeDA9KI42Iw2iy3WoCwN5F3t8.XI8m','2024-05-16 13:19:53','2024-05-16 13:19:53',''),(144,'nickasd','hi@naver.com','$2a$10$ufWiuc5jBfEeiY.Dsoz6cO.buiduIWP50/I8nsk/hLmqpex1.86vS','2024-05-19 00:58:59','2024-05-19 00:58:59',''),(145,'chlwodyd','chlwodyd@naver.com','$2a$10$QNvYA5etWsbpHYagxtPCmevTkPDqJfjMdvViVBVp2QRa.rSuVOtJW','2024-05-19 00:59:37','2024-05-19 00:59:37',''),(146,'jin','jinooyang@gmail.com','$2a$10$CI5jcZk5ma6qVVLlkJ1AXOz142mSzI5.N2xOnN1iUuoRE58Iq5Lou','2024-05-19 00:59:46','2024-05-19 00:59:46','');
/*!40000 ALTER TABLE `member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `module`
--

DROP TABLE IF EXISTS `module`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `module` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `project_id` bigint NOT NULL,
  `title` varchar(50) NOT NULL,
  `category` varchar(50) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `FK_MODULE_PROJECT_ID` (`project_id`),
  CONSTRAINT `FK_MODULE_PROJECT_ID` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=36 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `module`
--

LOCK TABLES `module` WRITE;
/*!40000 ALTER TABLE `module` DISABLE KEYS */;
INSERT INTO `module` VALUES (14,9,'ㅇㅇㅇ','API 명세서','2024-05-10 10:40:05','2024-05-10 10:40:05'),(15,9,'ㅂㅂㅂ','일정 관리','2024-05-10 10:40:16','2024-05-10 10:40:16'),(16,10,'asdasdasd','일정 관리','2024-05-10 13:14:41','2024-05-10 13:14:41'),(17,11,'ddd','일정 관리','2024-05-12 22:15:53','2024-05-12 22:15:53'),(18,11,'dddf','API 명세서','2024-05-12 22:16:02','2024-05-12 22:16:02'),(20,14,'1주차','일정 관리','2024-05-13 21:32:54','2024-05-13 21:32:54'),(21,14,'API V1','API 명세서','2024-05-13 21:33:15','2024-05-13 21:33:15'),(23,22,'f','API 명세서','2024-05-14 14:46:16','2024-05-14 14:46:16'),(24,21,'f','일정 관리','2024-05-14 14:46:24','2024-05-14 14:46:24'),(25,21,'fsdfsdf','API 명세서','2024-05-14 14:47:06','2024-05-14 14:47:06'),(26,24,'hihi','API 명세서','2024-05-14 16:42:27','2024-05-14 16:42:27'),(27,24,'hihihi','일정 관리','2024-05-14 16:42:55','2024-05-14 16:42:55'),(28,25,'ㄴㅇㄹㄴㄹㅇ','일정 관리','2024-05-16 09:04:52','2024-05-16 09:04:52'),(29,26,'아아','일정 관리','2024-05-16 09:39:56','2024-05-16 09:39:56'),(30,26,'qq','API 명세서','2024-05-16 09:41:29','2024-05-16 09:41:29'),(31,27,'1주차','일정 관리','2024-05-16 12:40:23','2024-05-16 12:40:23'),(32,27,'API 명세서','API 명세서','2024-05-16 12:48:32','2024-05-16 12:48:32'),(33,28,'포스트맨 파쿠리','API 명세서','2024-05-16 13:28:48','2024-05-16 13:28:48'),(34,31,'테스트','API 명세서','2024-05-17 13:05:20','2024-05-17 13:05:20'),(35,32,'qwe','API 명세서','2024-05-19 01:00:48','2024-05-19 01:00:48');
/*!40000 ALTER TABLE `module` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `module_category`
--

DROP TABLE IF EXISTS `module_category`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `module_category` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `category` varchar(50) NOT NULL,
  `path` varchar(20) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `module_category`
--

LOCK TABLES `module_category` WRITE;
/*!40000 ALTER TABLE `module_category` DISABLE KEYS */;
INSERT INTO `module_category` VALUES (1,'일정 관리','kanbanboards','2024-05-09 10:53:58','2024-05-09 10:53:58'),(2,'API 명세서','api-specifications','2024-05-09 10:53:58','2024-05-09 10:53:58');
/*!40000 ALTER TABLE `module_category` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `path_variable`
--

DROP TABLE IF EXISTS `path_variable`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `path_variable` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `api_id` bigint NOT NULL,
  `path` varchar(20) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `api_id` (`api_id`),
  CONSTRAINT `path_variable_ibfk_1` FOREIGN KEY (`api_id`) REFERENCES `api` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `path_variable`
--

LOCK TABLES `path_variable` WRITE;
/*!40000 ALTER TABLE `path_variable` DISABLE KEYS */;
INSERT INTO `path_variable` VALUES (19,16,NULL,'projectId','프로젝트의 ID 값입니다. (정수형)','2024-05-16 14:21:05','2024-05-16 14:21:05'),(20,16,NULL,'apiSpecificationId','API 명세서의 ID 값입니다. (정수형)','2024-05-16 14:21:05','2024-05-16 14:21:05'),(21,11,NULL,'projectId','프로젝트 PK, 정수형','2024-05-17 13:13:03','2024-05-17 13:13:03'),(22,11,NULL,'apiSpecificationId','API 명세서 PK, 정수형','2024-05-17 13:13:03','2024-05-17 13:13:03'),(23,13,NULL,'projectId','프로젝트 PK (정수형)','2024-05-17 13:13:21','2024-05-17 13:13:21'),(24,13,NULL,'apiSpecificationId','API 명세서 PK (정수형)','2024-05-17 13:13:21','2024-05-17 13:13:21'),(25,13,NULL,'domainId','도메인 PK (정수형)','2024-05-17 13:13:21','2024-05-17 13:13:21'),(26,12,NULL,'projectId','프로젝트 PK','2024-05-17 13:13:50','2024-05-17 13:13:50'),(27,12,NULL,'apiSpecificationId','API 명세서 PK','2024-05-17 13:13:50','2024-05-17 13:13:50'),(28,12,NULL,'domainId','도메인 PK','2024-05-17 13:13:50','2024-05-17 13:13:50'),(29,10,NULL,'projectId','프로젝트 PK (정수형)','2024-05-17 13:14:13','2024-05-17 13:14:13'),(30,10,NULL,'apiSpecificationId','API 명세서 PK (정수형)','2024-05-17 13:14:13','2024-05-17 13:14:13'),(31,10,NULL,'domainId','도메인 PK (정수형)','2024-05-17 13:14:13','2024-05-17 13:14:13'),(32,10,NULL,'apiId','API PK (정수형)','2024-05-17 13:14:13','2024-05-17 13:14:13');
/*!40000 ALTER TABLE `path_variable` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project`
--

DROP TABLE IF EXISTS `project`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(10) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=33 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project`
--

LOCK TABLES `project` WRITE;
/*!40000 ALTER TABLE `project` DISABLE KEYS */;
INSERT INTO `project` VALUES (9,'ㅇㅇ','2024-05-10 10:39:55','2024-05-10 10:39:55'),(10,'sadasdsad','2024-05-10 13:14:32','2024-05-10 13:14:32'),(11,'테스트','2024-05-12 22:14:41','2024-05-12 22:14:41'),(13,'여일구의삼성을','2024-05-13 12:37:54','2024-05-13 12:37:54'),(14,'Omess','2024-05-13 21:32:42','2024-05-13 21:32:42'),(15,'안녕 일구','2024-05-14 10:44:31','2024-05-14 10:44:31'),(16,'테스트','2024-05-14 10:45:59','2024-05-14 10:45:59'),(17,'test','2024-05-14 12:45:07','2024-05-14 12:45:07'),(19,'test','2024-05-14 13:49:27','2024-05-14 13:49:27'),(20,'김재찬','2024-05-14 13:50:21','2024-05-14 13:50:21'),(21,'ㄴㅇㄹㄴㄹㅇ','2024-05-14 14:45:31','2024-05-14 14:45:31'),(22,'ㄴㅇㄹㄴㅇㄹ','2024-05-14 14:45:43','2024-05-14 14:45:43'),(24,'하하','2024-05-14 16:41:15','2024-05-14 16:41:15'),(25,'ㄴㅇㄹㄴㄹㅇ','2024-05-16 09:04:46','2024-05-16 09:04:46'),(26,'아아','2024-05-16 09:37:39','2024-05-16 09:37:39'),(27,'Omess','2024-05-16 12:40:03','2024-05-16 12:40:03'),(28,'아즈카반','2024-05-16 13:24:10','2024-05-16 13:24:10'),(30,'sdfsf','2024-05-16 14:21:12','2024-05-16 14:21:12'),(31,'example','2024-05-17 13:05:05','2024-05-17 13:05:05'),(32,'테스트','2024-05-19 01:00:23','2024-05-19 01:00:23');
/*!40000 ALTER TABLE `project` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `project_member`
--

DROP TABLE IF EXISTS `project_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `project_member` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `member_id` bigint NOT NULL,
  `project_id` bigint NOT NULL,
  `project_role` enum('OWNER','USER') NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `member_id` (`member_id`),
  KEY `project_id` (`project_id`),
  CONSTRAINT `project_member_ibfk_1` FOREIGN KEY (`member_id`) REFERENCES `member` (`id`) ON DELETE CASCADE,
  CONSTRAINT `project_member_ibfk_2` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=55 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `project_member`
--

LOCK TABLES `project_member` WRITE;
/*!40000 ALTER TABLE `project_member` DISABLE KEYS */;
INSERT INTO `project_member` VALUES (9,132,9,'OWNER','2024-05-10 10:39:55','2024-05-10 10:39:55'),(10,133,10,'OWNER','2024-05-10 13:14:32','2024-05-10 13:14:32'),(11,126,11,'OWNER','2024-05-12 22:14:41','2024-05-12 22:14:41'),(14,126,13,'USER','2024-05-13 12:37:54','2024-05-13 12:37:54'),(15,133,14,'OWNER','2024-05-13 21:32:42','2024-05-13 21:32:42'),(16,1,14,'USER','2024-05-13 21:32:42','2024-05-13 21:32:42'),(17,121,14,'USER','2024-05-13 21:32:42','2024-05-13 21:32:42'),(18,127,15,'OWNER','2024-05-14 10:44:31','2024-05-14 10:44:31'),(19,134,16,'OWNER','2024-05-14 10:45:59','2024-05-14 10:45:59'),(20,127,16,'USER','2024-05-14 10:45:59','2024-05-14 10:45:59'),(22,135,17,'USER','2024-05-14 12:45:07','2024-05-14 12:45:07'),(26,136,19,'USER','2024-05-14 13:49:27','2024-05-14 13:49:27'),(28,136,20,'USER','2024-05-14 13:50:21','2024-05-14 13:50:21'),(30,1,21,'USER','2024-05-14 14:45:31','2024-05-14 14:45:31'),(32,121,22,'USER','2024-05-14 14:45:43','2024-05-14 14:45:43'),(35,137,24,'OWNER','2024-05-14 16:41:15','2024-05-14 16:41:15'),(36,126,24,'USER','2024-05-14 16:41:15','2024-05-14 16:41:15'),(38,1,25,'USER','2024-05-16 09:04:46','2024-05-16 09:04:46'),(39,133,26,'OWNER','2024-05-16 09:37:39','2024-05-16 09:37:39'),(40,125,26,'USER','2024-05-16 09:37:39','2024-05-16 09:37:39'),(41,142,27,'OWNER','2024-05-16 12:40:03','2024-05-16 12:40:03'),(42,141,27,'USER','2024-05-16 12:40:03','2024-05-16 12:40:03'),(43,126,28,'OWNER','2024-05-16 13:24:10','2024-05-16 13:24:10'),(44,143,28,'USER','2024-05-16 13:24:10','2024-05-16 13:24:10'),(45,130,27,'USER','2024-05-16 04:31:55','2024-05-16 04:31:55'),(46,124,27,'USER','2024-05-16 04:33:56','2024-05-16 04:33:56'),(49,121,30,'USER','2024-05-16 14:21:12','2024-05-16 14:21:12'),(50,124,31,'OWNER','2024-05-17 13:05:05','2024-05-17 13:05:05'),(51,126,31,'USER','2024-05-17 13:05:05','2024-05-17 13:05:05'),(52,145,32,'OWNER','2024-05-19 01:00:23','2024-05-19 01:00:23'),(53,124,32,'USER','2024-05-19 01:00:23','2024-05-19 01:00:23'),(54,1,32,'USER','2024-05-19 01:00:23','2024-05-19 01:00:23');
/*!40000 ALTER TABLE `project_member` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `query_param`
--

DROP TABLE IF EXISTS `query_param`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `query_param` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `api_id` bigint NOT NULL,
  `path` varchar(20) DEFAULT NULL,
  `name` varchar(20) NOT NULL,
  `description` varchar(50) DEFAULT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `api_id` (`api_id`),
  CONSTRAINT `query_param_ibfk_1` FOREIGN KEY (`api_id`) REFERENCES `api` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=17 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `query_param`
--

LOCK TABLES `query_param` WRITE;
/*!40000 ALTER TABLE `query_param` DISABLE KEYS */;
INSERT INTO `query_param` VALUES (15,14,NULL,'id','파일 참조 ID (referenceId, 문자열)','2024-05-16 13:17:11','2024-05-16 13:17:11'),(16,14,NULL,'type','파일 참조 타입(ISSUE, PROFILE_IMAGE, CHAT)','2024-05-16 13:17:11','2024-05-16 13:17:11');
/*!40000 ALTER TABLE `query_param` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `request_header`
--

DROP TABLE IF EXISTS `request_header`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `request_header` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `api_id` bigint NOT NULL,
  `path` varchar(20) DEFAULT NULL,
  `header_key` varchar(50) NOT NULL,
  `header_value` varchar(100) NOT NULL,
  `created_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_at` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `api_id` (`api_id`),
  CONSTRAINT `request_header_ibfk_1` FOREIGN KEY (`api_id`) REFERENCES `api` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `request_header`
--

LOCK TABLES `request_header` WRITE;
/*!40000 ALTER TABLE `request_header` DISABLE KEYS */;
/*!40000 ALTER TABLE `request_header` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-05-19  3:40:53
